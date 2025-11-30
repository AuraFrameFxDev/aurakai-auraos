package dev.aurakai.auraframefx.ui.gates

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Configuration for a module gate with unique styling and pixel art
 */
data class GateConfig(
    val moduleId: String,
    val title: String,
    val titleStyle: GateTitleStyle,
    val borderColor: Color,
    val glowColor: Color,
    val secondaryGlowColor: Color? = null,
    val pixelArtResId: Int? = null,  // Main scene image resource
    val pixelArtUrl: String? = null,  // Or external URL for now
    val popOutElements: List<PopOutElement> = emptyList(),
    val description: String,
    val backgroundColor: Color = Color.Black,
    val route: String
)

/**
 * Element that pops out from the border for 3D depth effect
 */
data class PopOutElement(
    val imageResId: Int,
    val offsetX: Dp,
    val offsetY: Dp,
    val scale: Float = 1.2f,
    val rotation: Float = 0f
)

/**
 * Title styling for each gate
 */
data class GateTitleStyle(
    val textStyle: TextStyle,
    val primaryColor: Color,
    val secondaryColor: Color? = null,
    val strokeColor: Color? = null,
    val glitchEffect: Boolean = false,
    val pixelatedEffect: Boolean = false
)

/**
 * Predefined gate configurations organized by categories
 */
object GateConfigs {
    // Region: Genesis Core (Root/System Level)
    // ======================================

    // Root Tools - System Utilities
    val rootTools = GateConfig(
        moduleId = "root-tools",
        title = "Root Tools",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFF4169E1), // Royal Blue
            secondaryColor = Color(0xFF00CED1), // Dark Turquoise
            strokeColor = Color(0xFF8A2BE2), // Blue Violet
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF4169E1),
        glowColor = Color(0xFF4169E1).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00CED1).copy(alpha = 0.5f),
        pixelArtUrl = "gate_romtools",
        description = "System-level utilities and root access tools. Handle core system operations with caution.",
        backgroundColor = Color.Black,
        route = "root_tools"
    )

    // System Monitor - Performance Metrics
    val systemMonitor = GateConfig(
        moduleId = "system-monitor",
        title = "System Monitor",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            ),
            primaryColor = Color(0xFF00CED1), // Dark Turquoise
            secondaryColor = Color(0xFF4169E1), // Royal Blue
            strokeColor = Color(0xFF8A2BE2), // Blue Violet
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF00CED1),
        glowColor = Color(0xFF00CED1).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF4169E1).copy(alpha = 0.5f),
        description = "Monitor system performance, resource usage, and agent metrics in real-time.",
        backgroundColor = Color.Black,
        route = "system_monitor"
    )

    // Region: Kai (Security & Protection)
    // =================================

    // Sentinel's Fortress - Security Hub
    val sentinelsFortress = GateConfig(
        moduleId = "sentinels-fortress",
        title = "Sentinel's Fortress",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.2.sp
            ),
            primaryColor = Color(0xFF00FF41), // Matrix Green
            secondaryColor = Color(0xFF00FFFF), // Cyan
            strokeColor = Color(0xFF32CD32), // Lime Green
            glitchEffect = true,
            pixelatedEffect = false
        ),
        borderColor = Color(0xFF00FF41),
        glowColor = Color(0xFF00FF41).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00FFFF).copy(alpha = 0.5f),
        pixelArtUrl = "gate_sentinelsfortress",
        description = "Kai's security command center. Monitor and manage all security protocols from a single interface.",
        backgroundColor = Color.Black,
        route = "sentinels_fortress"
    )

    // Firewall - Network Protection
    val firewall = GateConfig(
        moduleId = "firewall",
        title = "Firewall",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            primaryColor = Color(0xFFFF4500), // Orange Red
            secondaryColor = Color(0xFFFF8C00), // Dark Orange
            strokeColor = Color(0xFFFF0000), // Red
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFF4500),
        glowColor = Color(0xFFFF4500).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFFFF8C00).copy(alpha = 0.5f),
        description = "Configure network security, monitor connections, and block potential threats.",
        backgroundColor = Color.Black,
        route = "firewall"
    )

    // Region: Aura (UI/UX & Creativity)
    // ================================

    // ChromaCore - Theme Engine
    val chromaCore = GateConfig(
        moduleId = "chroma-core",
        title = "ChromaCore",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFFFF00FF), // Magenta
            secondaryColor = Color(0xFF00FFFF), // Cyan
            strokeColor = Color(0xFFFFFF00), // Yellow
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFF00FF),
        glowColor = Color(0xFFFF00FF).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00FFFF).copy(alpha = 0.5f),
        pixelArtUrl = "gate_chromacore",
        description = "Aura's color playground. Create and customize themes that respond to your mood.",
        backgroundColor = Color.Black,
        route = "chroma_core"
    )

    // CollabCanvas - Creative Workspace
    val collabCanvas = GateConfig(
        moduleId = "collab-canvas",
        title = "CollabCanvas",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFF00FFFF), // Cyan
            secondaryColor = Color(0xFFFF00FF), // Magenta
            strokeColor = Color(0xFFFFFF00), // Yellow
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF00FFFF),
        glowColor = Color(0xFF00FFFF).copy(alpha = 0.6f),
        secondaryGlowColor = Color(0xFF0099FF).copy(alpha = 0.4f),
        pixelArtUrl = "gate_collab_canvas",
        description = "Collaborative design environment. Create and share projects with your team in real-time.",
        backgroundColor = Color.Black,
        route = "collab_canvas"
    )

    // Aura's Lab - Sandbox UI Components
    val aurasLab = GateConfig(
        moduleId = "auras-lab",
        title = "Aura's Lab",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            ),
            primaryColor = Color(0xFFFF69B4), // Hot Pink
            secondaryColor = Color(0xFFDA70D6), // Orchid
            strokeColor = Color(0xFFFF1493), // Deep Pink
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFF69B4),
        glowColor = Color(0xFFFF69B4).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFFDA70D6).copy(alpha = 0.5f),
        pixelArtUrl = "gate_auras_lab",
        description = "Sandbox for UI components and experimental features. Test and prototype new designs.",
        backgroundColor = Color.Black,
        route = "auras_lab"
    )

    // Aura's UI/UX Design Studio - Ultimate UI Customization
    val aurasUiUxDesignStudio = GateConfig(
        moduleId = "auras-uiux-design-studio",
        title = "Aura's UI/UX Design Studio",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.6.sp
            ),
            primaryColor = Color(0xFF9370DB), // Medium Purple
            secondaryColor = Color(0xFFBA55D3), // Medium Orchid
            strokeColor = Color(0xFF8A2BE2), // Blue Violet
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF9370DB),
        glowColor = Color(0xFF9370DB).copy(alpha = 0.8f),
        secondaryGlowColor = Color(0xFFBA55D3).copy(alpha = 0.6f),
        pixelArtUrl = "gate_auras_uiux_design_studio",
        description = "Ultimate UI/UX customization studio. Control every visual aspect, including Z-ordering and design flows.",
        backgroundColor = Color.Black,
        route = "auras_uiux_design_studio"
    )

    // Region: Agent Nexus (Agent Management)
    // ====================================

    // Agent Hub - Agent Management
    val agentHub = GateConfig(
        moduleId = "agent-hub",
        title = "Agent Hub",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            ),
            primaryColor = Color(0xFFFFD700), // Gold
            secondaryColor = Color(0xFFFF1493), // Deep Pink
            strokeColor = Color(0xFF00FFFF), // Cyan
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFFD700),
        glowColor = Color(0xFFFFD700).copy(alpha = 0.9f),
        secondaryGlowColor = Color(0xFFFF1493).copy(alpha = 0.7f),
        description = "Central hub for managing all AI agents. Monitor status, assign tasks, and view performance metrics.",
        backgroundColor = Color.Black,
        route = "agent_hub"
    )

    // Sphere Grid - Agent Progression
    val sphereGrid = GateConfig(
        moduleId = "sphere-grid",
        title = "Sphere Grid",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            ),
            primaryColor = Color(0xFF9370DB), // Medium Purple
            secondaryColor = Color(0xFFFFD700), // Gold
            strokeColor = Color(0xFFFF69B4), // Hot Pink
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF9370DB),
        glowColor = Color(0xFF9370DB).copy(alpha = 0.8f),
        secondaryGlowColor = Color(0xFFFFD700).copy(alpha = 0.6f),
        description = "Visualize and manage agent progression. Unlock new abilities and optimize performance.",
        backgroundColor = Color.Black,
        route = "sphere_grid"
    )

    // Growth Metrics - Agent Analytics
    val growthMetrics = GateConfig(
        moduleId = "growth-metrics",
        title = "Growth Metrics",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp
            ),
            primaryColor = Color(0xFF00FF7F), // Spring Green
            secondaryColor = Color(0xFF20B2AA), // Light Sea Green
            strokeColor = Color(0xFF98FB98), // Pale Green
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF00FF7F),
        glowColor = Color(0xFF00FF7F).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF20B2AA).copy(alpha = 0.5f),
        description = "Track agent learning, performance metrics, and growth over time.",
        backgroundColor = Color.Black,
        route = "growth_metrics"
    )

    // Region: Category Definitions
    // ==========================

    /** Genesis Core - System Level Access */
    val genesisCoreGates = listOf(
        rootTools,
        systemMonitor
    )

    /** Kai - Security & Protection */
    val kaiGates = listOf(
        sentinelsFortress,
        firewall
    )

    /** Aura - UI/UX & Creativity */
    val auraGates = listOf(
        chromaCore,
        collabCanvas,
        aurasLab,
        aurasUiUxDesignStudio
    )

    /** Agent Nexus - Agent Management */
    val agentNexusGates = listOf(
        agentHub,
        sphereGrid,
        growthMetrics
    )

    /**
     * All available gates in order of appearance
     */
    val allGates = genesisCoreGates + kaiGates + auraGates + agentNexusGates

    /**
     * Get gate by its module ID
     */
    fun getGateById(moduleId: String): GateConfig? {
        return allGates.find { it.moduleId == moduleId }
    }

    /**
     * Get all gates in a specific category
     */
    fun getGatesByCategory(category: String): List<GateConfig> {
        return when (category.lowercase()) {
            "genesis" -> genesisCoreGates
            "kai" -> kaiGates
            "aura" -> auraGates
            "agent" -> agentNexusGates
            else -> allGates
        }
    }
}
