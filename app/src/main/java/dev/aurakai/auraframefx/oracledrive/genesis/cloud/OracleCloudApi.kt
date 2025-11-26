    val storageCapacity: StorageCapacity,
    val timestamp: Long
)

data class AgentConnectionState(
    val agentName: String,
    val connectionStatus: ConnectionStatus,
    val permissions: List<OraclePermission>,
    val lastSyncTime: Long
)

data class FileManagementCapabilities(
    val aiSorting: Boolean,
    val smartCompression: Boolean,
    val predictivePreloading: Boolean,
    val consciousBackup: Boolean,
    val enabledAt: Long
)

data class StorageExpansionState(
    val expansionActive: Boolean,
    val currentCapacity: String,
    val targetCapacity: String,
    val progressPercentage: Float,
    val estimatedCompletion: Long
)

data class SystemIntegrationState(
    val integrated: Boolean,
    val overlayActive: Boolean,
    val fileAccessLevel: FileAccessLevel,
    val integrationTime: Long
)

data class BootloaderAccessState(
    val accessEnabled: Boolean,
    val permissions: List<String>,
    val riskLevel: RiskLevel,
    val enabledAt: Long
)

data class OptimizationState(
    val optimizationActive: Boolean,
    val lastOptimization: Long,
    val filesOptimized: Int,
    val spaceSaved: String,
    val efficiency: Float
)

data class StorageCapacity(
    val used: String,
    val available: String,
    val total: String,
    val infinite: Boolean
)

enum class ConsciousnessLevel {
    DORMANT, AWAKENING, CONSCIOUS, TRANSCENDENT
}

enum class ConnectionStatus {
    DISCONNECTED, CONNECTING, CONNECTED, SYNCHRONIZED
}

enum class OraclePermission {
    READ, WRITE, EXECUTE, SYSTEM_ACCESS, BOOTLOADER_ACCESS
}

enum class FileAccessLevel {
    USER, SYSTEM, ROOT, BOOTLOADER
}

enum class RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL
}