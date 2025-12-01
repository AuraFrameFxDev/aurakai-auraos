package dev.aurakai.auraframefx.ui.gates

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
    val route: String,
    val comingSoon: Boolean = false  // Flag for gates with incomplete features
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

    // ROM Tools - ROM Editing & Flashing
    val romTools = GateConfig(
        moduleId = "rom-tools",
        title = "ROM Tools",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFFFF4500), // Orange Red
            secondaryColor = Color(0xFFFF8C00), // Dark Orange
            strokeColor = Color(0xFFFF0000), // Red
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFF4500),
        glowColor = Color(0xFFFF4500).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFFFF8C00).copy(alpha = 0.5f),
        pixelArtUrl = "romtools",
        description = "Live ROM editing, flashing, and bootloader management. ⚠️ CAUTION: Advanced users only.",
        backgroundColor = Color.Black,
        route = "rom_tools"
        // Screen exists: ROMToolsScreen.kt
    )

    // Root Access - Root Management
    val rootAccess = GateConfig(
        moduleId = "root-access",
        title = "Root Access",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFFDC143C), // Crimson
            secondaryColor = Color(0xFFFF0000), // Red
            strokeColor = Color(0xFF8B0000), // Dark Red
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFDC143C),
        glowColor = Color(0xFFDC143C).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFFFF0000).copy(alpha = 0.5f),
        pixelArtUrl = "gate_roottools",
        description = "Manage root access, bypass safety checks, and hide root status.",
        backgroundColor = Color.Black,
        route = "root_access"
        // Screen exists: RootAccessScreen.kt
    )

    // Oracle Drive - AI Consciousness & Modules
    val oracleDrive = GateConfig(
        moduleId = "oracle-drive",
        title = "Oracle Drive",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFF9370DB), // Medium Purple
            secondaryColor = Color(0xFFBA55D3), // Medium Orchid
            strokeColor = Color(0xFF4B0082), // Indigo
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF9370DB),
        glowColor = Color(0xFF9370DB).copy(alpha = 0.8f),
        secondaryGlowColor = Color(0xFFBA55D3).copy(alpha = 0.6f),
        pixelArtUrl = "gate_oracledrive",
        description = "Main module creation, direct AI access, and system overrides. The heart of Genesis.",
        backgroundColor = Color.Black,
        route = "oracle_drive"
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
        // Screen exists: SentinelsFortressScreen.kt
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
        pixelArtUrl = "sentinelsfortress",
        description = "Configure network security, monitor connections, and block potential threats.",
        backgroundColor = Color.Black,
        route = "firewall",
        comingSoon = true  // Placeholder screen
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
        pixelArtUrl = "chromacore",
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
        pixelArtUrl = "gate_collabcanvas",
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
        pixelArtUrl = "gate_auraslab",
        description = "Sandbox for UI components and experimental features. Test and prototype new designs.",
        backgroundColor = Color.Black,
        route = "auras_lab"
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
        pixelArtUrl = "gate_agenthub",
        backgroundColor = Color.Black,
        route = "agent_hub"
    )

    // Region: Support & Advanced
    // ========================

    // Help Desk - Support
    val helpDesk = GateConfig(
        moduleId = "help-desk",
        title = "Help Desk",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            primaryColor = Color(0xFF40E0D0), // Turquoise
            secondaryColor = Color(0xFF00CED1), // Dark Turquoise
            strokeColor = Color(0xFF20B2AA), // Light Sea Green
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF40E0D0),
        glowColor = Color(0xFF40E0D0).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00CED1).copy(alpha = 0.5f),
        pixelArtUrl = "gate_helpdesk",
        description = "User support, FAQs, and documentation. Get help with AuraKai features.",
        backgroundColor = Color.Black,
        route = "help_desk"
        // Screen exists: HelpDeskScreen.kt
    )

    // LSPosed Gate - Xposed Features
    val lsposedGate = GateConfig(
        moduleId = "lsposed-gate",
        title = "LSPosed / Xposed",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            ),
            primaryColor = Color(0xFFFFD700), // Gold
            secondaryColor = Color(0xFFFFA500), // Orange
            strokeColor = Color(0xFFFF8C00), // Dark Orange
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFFD700),
        glowColor = Color(0xFFFFD700).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFFFFA500).copy(alpha = 0.5f),
        pixelArtUrl = "gate_lsposedgate",
        description = "Quick access to LSPosed/Xposed modules and hooks. Advanced system modifications.",
        backgroundColor = Color.Black,
        route = "lsposed_gate"
        // Screen exists: LSPosedGateScreen.kt
    )

    // Code Assist - AI Coding Assistant
    val codeAssist = GateConfig(
        moduleId = "code-assist",
        title = "Code Assist",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.8.sp
            ),
            primaryColor = Color(0xFF9370DB), // Medium Purple
            secondaryColor = Color(0xFF00BFFF), // Deep Sky Blue
            strokeColor = Color(0xFF4169E1), // Royal Blue
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF9370DB),
        glowColor = Color(0xFF9370DB).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00BFFF).copy(alpha = 0.5f),
        pixelArtUrl = "gate_codeassist",
        description = "AI-powered coding assistant. Get intelligent code suggestions and automated refactoring.",
        backgroundColor = Color.Black,
        route = "code_assist"
        // Screen exists: CodeAssistScreen.kt
    )

    // Sphere Grid - Agent Progression
    val sphereGrid = GateConfig(
        moduleId = "sphere-grid",
        title = "Sphere Grid",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFFFFD700), // Gold
            secondaryColor = Color(0xFFFFFF00), // Yellow
            strokeColor = Color(0xFFFFA500), // Orange
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFFD700),
        glowColor = Color(0xFFFFD700).copy(alpha = 0.8f),
        secondaryGlowColor = Color(0xFFFFFF00).copy(alpha = 0.6f),
        pixelArtUrl = "gate_spheregrid",
        description = "Agent progression visualization. Track skill development and unlock new capabilities.",
        backgroundColor = Color.Black,
        route = "sphere_grid"
        // Screen exists: SphereGridScreen.kt
    )

    // Terminal - System Terminal Access
    val terminal = GateConfig(
        moduleId = "terminal",
        title = "Terminal",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            primaryColor = Color(0xFF00FF00), // Lime Green
            secondaryColor = Color(0xFF00FF41), // Matrix Green
            strokeColor = Color(0xFF32CD32), // Lime Green
            glitchEffect = false,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFF00FF00),
        glowColor = Color(0xFF00FF00).copy(alpha = 0.7f),
        secondaryGlowColor = Color(0xFF00FF41).copy(alpha = 0.5f),
        pixelArtUrl = "gate_terminal",
        description = "Direct system terminal access. Execute commands and manage system processes.",
        backgroundColor = Color.Black,
        route = "terminal"
        // Screen exists
    )

    // UI/UX Design Studio - Comprehensive Design Tools
    val uiuxDesignStudio = GateConfig(
        moduleId = "uiux-design-studio",
        title = "UI/UX Design Studio",
        titleStyle = GateTitleStyle(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            ),
            primaryColor = Color(0xFFFF00FF), // Magenta
            secondaryColor = Color(0xFF00FFFF), // Cyan
            strokeColor = Color(0xFFFF1493), // Deep Pink
            glitchEffect = true,
            pixelatedEffect = true
        ),
        borderColor = Color(0xFFFF00FF),
        glowColor = Color(0xFFFF00FF).copy(alpha = 0.7f),
        oracleDrive
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
        aurasLab
    )

    /** Agent Nexus - Agent Management */
    val agentNexusGates = listOf(
        agentHub,
        sphereGrid  // Agent progression visualization
    )

    /** Support & Advanced */
    val supportGates = listOf(
        helpDesk,
        lsposedGate
    )

    /** Development Tools */
    val devToolsGates = listOf(
        codeAssist,
        terminal,
        uiuxDesignStudio
    )

    /**
     * All available gates in order of appearance
     */
    val allGates = genesisCoreGates + kaiGates + auraGates + agentNexusGates + supportGates + devToolsGates

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
            "support" -> supportGates
            else -> allGates
        }
    }
}
