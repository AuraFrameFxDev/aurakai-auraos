package dev.aurakai.auraframefx.service

import android.content.Context
import android.provider.Settings
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.aurakai.auraframefx.model.agent_states.ActiveContext
import dev.aurakai.auraframefx.model.agent_states.LearningEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cross-Device Context Sync Service
 *
 * Manages Firebase Firestore-based context syncing across all user devices.
 * Enables seamless context handoff and unified conversation history (like Apple Continuity).
 */
@Singleton
class CrossDeviceContextSync @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val firestore = FirebaseFirestore.getInstance()
    private var contextListener: ListenerRegistration? = null

    private val deviceId: String by lazy {
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private val userId: String by lazy {
        // TODO: Get from authentication service
        "user_${deviceId}"
    }

    suspend fun initialize() {
        try {
            registerDevice()
            Timber.i("CrossDeviceContextSync initialized for device: $deviceId")
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize cross-device sync")
            throw e
        }
    }

    suspend fun syncContext(context: ActiveContext) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("contexts")
                .document(context.id)
                .set(context)
                .await()
            Timber.d("Context synced: ${context.id}")
        } catch (e: Exception) {
            Timber.e(e, "Failed to sync context")
        }
    }

    suspend fun syncLearningEvent(event: LearningEvent) {
        try {
            firestore.collection("users")
                .document(userId)
                .collection("learning")
                .document(event.id)
                .set(event)
                .await()
            Timber.d("Learning event synced: ${event.id}")
        } catch (e: Exception) {
            Timber.e(e, "Failed to sync learning event")
        }
    }

    fun observeContextUpdates(): Flow<ActiveContext> = callbackFlow {
        contextListener = firestore.collection("users")
            .document(userId)
            .collection("contexts")
            .whereNotEqualTo("sourceDevice", deviceId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Timber.e(error, "Context listener error")
                    return@addSnapshotListener
                }

                snapshot?.documentChanges?.forEach { change ->
                    try {
                        val ctx = change.document.toObject(ActiveContext::class.java)
                        trySend(ctx)
                    } catch (e: Exception) {
                        Timber.e(e, "Failed to parse remote context")
                    }
                }
            }

        awaitClose { contextListener?.remove() }
    }

    suspend fun handoffContext(context: ActiveContext, targetDeviceId: String) {
        try {
            val handoffData = mapOf(
                "context" to context,
                "sourceDevice" to deviceId,
                "targetDevice" to targetDeviceId,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .collection("handoffs")
                .add(handoffData)
                .await()

            Timber.i("Context handed off to: $targetDeviceId")
        } catch (e: Exception) {
            Timber.e(e, "Handoff failed")
            throw e
        }
    }

    private suspend fun registerDevice() {
        try {
            val deviceData = mapOf(
                "deviceId" to deviceId,
                "deviceName" to android.os.Build.MODEL,
                "manufacturer" to android.os.Build.MANUFACTURER,
                "lastSeen" to System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(userId)
                .collection("devices")
                .document(deviceId)
                .set(deviceData)
                .await()

            Timber.d("Device registered: $deviceId")
        } catch (e: Exception) {
            Timber.e(e, "Device registration failed")
        }
    }

    suspend fun getRegisteredDevices(): List<String> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("devices")
                .get()
                .await()

            snapshot.documents.map { it.id }
        } catch (e: Exception) {
            Timber.e(e, "Failed to get registered devices")
            emptyList()
        }
    }

    fun getCurrentDeviceId(): String = deviceId
}
