package com.premsingh.a11ylens.kmp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private enum class LensTab(val label: String, val glyph: String) {
    Preview("Preview", "▣"), Contrast("Contrast", "◒"), Targets("Targets", "⊞"), Guide("Guide", "✓"),
}

private data class LensPalette(
    val name: String,
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val ink: Color,
    val muted: Color,
    val accent: Color,
    val dark: Boolean,
)

private val palettes = listOf(
    LensPalette("Studio", Color(0xFF315BA8), Color(0xFFE8EFFB), Color(0xFFF5F7FB), Color.White, Color(0xFF1C2B42), Color(0xFF63738A), Color(0xFF93E0C2), false),
    LensPalette("Sprout", Color(0xFF246D60), Color(0xFFE4F2ED), Color(0xFFF5F8F5), Color.White, Color(0xFF203832), Color(0xFF617A72), Color(0xFFE7B16A), false),
    LensPalette("Night", Color(0xFF9CBFFF), Color(0xFF263B58), Color(0xFF141F30), Color(0xFF1E2B3E), Color(0xFFE9F0FC), Color(0xFFAFBBD0), Color(0xFF9AE3C2), true),
)

private val contrastPairs = listOf(
    ContrastPair("Deep blue on white", Color(0xFF1C2B42), Color.White),
    ContrastPair("Ocean on mist", Color(0xFF315BA8), Color(0xFFE8EFFB)),
    ContrastPair("Soft gray on white", Color(0xFFAAB3C0), Color.White),
)

@Composable
fun App() {
    var selectedTab by remember { mutableStateOf(LensTab.Preview) }
    var paletteIndex by remember { mutableStateOf(0) }
    var contrastIndex by remember { mutableStateOf(0) }
    var targetSize by remember { mutableStateOf(44) }
    var largeText by remember { mutableStateOf(false) }
    var highContrast by remember { mutableStateOf(false) }
    val checkedGuidance = remember { mutableStateListOf(false, true, false, false, true) }
    val palette = palettes[paletteIndex]
    val scheme = if (palette.dark) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = Color(0xFF17263A),
            secondary = palette.secondary,
            background = palette.background,
            surface = palette.surface,
            onSurface = palette.ink,
            onBackground = palette.ink,
            surfaceVariant = palette.secondary,
            onSurfaceVariant = palette.muted,
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = Color.White,
            secondary = palette.secondary,
            background = palette.background,
            surface = palette.surface,
            onSurface = palette.ink,
            onBackground = palette.ink,
            surfaceVariant = palette.secondary,
            onSurfaceVariant = palette.muted,
        )
    }

    MaterialTheme(colorScheme = scheme) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Column(
                    Modifier.fillMaxHeight()
                        .widthIn(max = 900.dp)
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                        .padding(horizontal = 22.dp),
                ) {
                    BrandHeader(palette, largeText) {
                        paletteIndex = (paletteIndex + 1) % palettes.size
                    }
                    Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())) {
                        Spacer(Modifier.height(16.dp))
                        when (selectedTab) {
                            LensTab.Preview -> PreviewScreen(palette, largeText, highContrast, { largeText = it }, { highContrast = it })
                            LensTab.Contrast -> ContrastScreen(palette, largeText, contrastIndex) { contrastIndex = it }
                            LensTab.Targets -> TargetScreen(palette, largeText, targetSize) { targetSize = it }
                            LensTab.Guide -> GuideScreen(palette, largeText, checkedGuidance) { index ->
                                checkedGuidance[index] = !checkedGuidance[index]
                            }
                        }
                        Spacer(Modifier.height(18.dp))
                        Text(
                            "A PRACTICAL CHECKLIST, NOT AN AUTOMATED AUDIT",
                            Modifier.fillMaxWidth().padding(bottom = 10.dp),
                            color = palette.muted,
                            fontSize = 9.sp,
                            letterSpacing = 1.1.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                    NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                        LensTab.entries.forEach { tab ->
                            NavigationBarItem(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                icon = { Text(tab.glyph, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                                label = { Text(tab.label, fontSize = 10.sp) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandHeader(palette: LensPalette, largeText: Boolean, onThemeClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = palette.primary, shape = RoundedCornerShape(14.dp), modifier = Modifier.size(44.dp)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("◉", color = if (palette.dark) Color(0xFF17263A) else Color.White, fontSize = 23.sp, fontWeight = FontWeight.Black)
                }
            }
            Spacer(Modifier.width(11.dp))
            Column {
                Text("A11y Lens", color = palette.ink, fontSize = (19f * scale(largeText)).sp, fontWeight = FontWeight.Bold)
                Text("DESIGN FOR MORE PEOPLE", color = palette.muted, fontSize = 8.sp, letterSpacing = 1.25.sp)
            }
        }
        Surface(
            color = palette.secondary,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.clickable(onClick = onThemeClick)
                .semantics { contentDescription = "Change theme. Current theme: ${palette.name}" },
        ) {
            Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("◐", color = palette.primary, fontSize = 15.sp)
                Spacer(Modifier.width(6.dp))
                Text(palette.name, color = palette.ink, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PreviewScreen(
    palette: LensPalette,
    largeText: Boolean,
    highContrast: Boolean,
    onLargeTextChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
) {
    val textScale = scale(largeText)
    var showDetails by remember { mutableStateOf(false) }
    Eyebrow("INTERACTIVE PREVIEW", palette)
    Spacer(Modifier.height(5.dp))
    Title("See your screen\nthrough a new lens.", palette, largeText)
    Text("Turn on a setting and explore the changes right here.", color = palette.muted, fontSize = (12f * textScale).sp)
    Spacer(Modifier.height(15.dp))
    Surface(
        color = if (highContrast) Color.Black else palette.primary,
        contentColor = Color.White,
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("COMMUNITY · THIS WEEK", color = Color(0xFFDDE9FF), fontSize = (9f * textScale).sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text("A small step,\nshared by all.", color = Color.White, fontSize = (27f * textScale).sp, lineHeight = (32f * textScale).sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Join a neighborhood clean-up this Saturday. Everyone is welcome.", color = Color(0xFFE6EDFA), fontSize = (13f * textScale).sp, lineHeight = (19f * textScale).sp)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { showDetails = !showDetails },
                modifier = Modifier.fillMaxWidth().height(50.dp).semantics { contentDescription = "View event details" },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (highContrast) Color(0xFFFFD15C) else Color(0xFF9AE3C2), contentColor = Color(0xFF172637)),
            ) {
                Text(if (showDetails) "Hide event details" else "View event details", fontSize = (13f * textScale).sp, fontWeight = FontWeight.Bold)
            }
            if (showDetails) {
                Spacer(Modifier.height(12.dp))
                Text("Saturday · 9:00 AM · Riverside Park. Step-free paths and a quiet meeting point are available.", color = Color.White, fontSize = (12f * textScale).sp, lineHeight = (18f * textScale).sp)
            }
        }
    }
    Spacer(Modifier.height(15.dp))
    SectionHeading("Try an accessibility setting", "CHANGE THE PREVIEW", palette, largeText)
    Spacer(Modifier.height(6.dp))
    PreferenceRow("Larger text", "Scale the sample reading text.", largeText, onLargeTextChange, palette, largeText)
    PreferenceRow("High contrast", "Strengthen the card’s foreground contrast.", highContrast, onHighContrastChange, palette, largeText)
    Spacer(Modifier.height(12.dp))
    InfoCard("Preview limits", "Real devices still matter", "Also check screen readers, magnification, and actual touch use.", palette)
}

@Composable
private fun ContrastScreen(palette: LensPalette, largeText: Boolean, selected: Int, onSelect: (Int) -> Unit) {
    Eyebrow("COLOR & READABILITY", palette)
    Title("Does this text\nstand out enough?", palette, largeText)
    Text("Choose a pair to calculate its contrast ratio.", color = palette.muted, fontSize = (12f * scale(largeText)).sp)
    Spacer(Modifier.height(14.dp))
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        contrastPairs.forEachIndexed { index, pair ->
            ChoiceChip(pair.name, selected == index, palette) { onSelect(index) }
        }
    }
    Spacer(Modifier.height(14.dp))
    val pair = contrastPairs[selected]
    val ratio = contrastRatio(pair.foreground, pair.background)
    Surface(color = pair.background, shape = RoundedCornerShape(23.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(19.dp)) {
            Text("BODY TEXT SAMPLE", color = pair.foreground, fontSize = 9.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(9.dp))
            Text("Readable in a quick glance.", color = pair.foreground, fontSize = (20f * scale(largeText)).sp, lineHeight = (26f * scale(largeText)).sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text("Clear text helps people read comfortably in different lighting and at different sizes.", color = pair.foreground, fontSize = (13f * scale(largeText)).sp, lineHeight = (19f * scale(largeText)).sp)
            Spacer(Modifier.height(15.dp))
            Text(roundedRatio(ratio) + " : 1", color = pair.foreground, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(pair.name, color = pair.foreground, fontSize = 11.sp)
        }
    }
    Spacer(Modifier.height(13.dp))
    ThresholdRow("Normal text", "4.5 : 1", ratio >= 4.5, palette, largeText)
    ThresholdRow("Large text", "3 : 1", ratio >= 3.0, palette, largeText)
    Spacer(Modifier.height(10.dp))
    InfoCard("WCAG reference", "Check the actual text size", "Large text has a lower contrast threshold. This demo uses the common AA ratios.", palette)
}

@Composable
private fun TargetScreen(palette: LensPalette, largeText: Boolean, selected: Int, onSelect: (Int) -> Unit) {
    Eyebrow("TOUCH & MOTOR", palette)
    Title("Give every tap\nroom to land.", palette, largeText)
    Text("Tap a sample target. The app recommends at least 48 dp.", color = palette.muted, fontSize = (12f * scale(largeText)).sp)
    Spacer(Modifier.height(14.dp))
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(17.dp)) {
            Text("TARGET SIZE INSPECTOR", color = palette.primary, fontSize = 9.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(17.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                listOf(36, 44, 48, 56).forEach { size ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            Modifier.size(size.dp).clip(CircleShape)
                                .background(if (size >= 48) palette.primary else Color(0xFFC45D54))
                                .clickable { onSelect(size) }
                                .semantics { contentDescription = "${size}dp sample touch target" },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(if (selected == size) "✓" else "·", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(7.dp))
                        Text("${size}dp", color = palette.ink, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            val meetsRecommendation = selected >= 48
            StatusLine(
                if (meetsRecommendation) "Meets 48 dp guidance" else "Below 48 dp guidance",
                meetsRecommendation,
                palette,
                largeText,
            )
        }
    }
    Spacer(Modifier.height(13.dp))
    SectionHeading("A few things to inspect", "BEYOND THE NUMBER", palette, largeText)
    Spacer(Modifier.height(7.dp))
    BulletCard("Spacing", "Leave enough distance between nearby actions.", "↔", palette, largeText)
    BulletCard("Reach", "Place frequent actions where they are easy to reach.", "◎", palette, largeText)
    BulletCard("Feedback", "Show a visible response after each activation.", "✦", palette, largeText)
}

@Composable
private fun GuideScreen(palette: LensPalette, largeText: Boolean, checks: List<Boolean>, onToggle: (Int) -> Unit) {
    val textScale = scale(largeText)
    Eyebrow("TEAM HANDOFF", palette)
    Title("A better review\nstarts with a habit.", palette, largeText)
    Text("Use this quick pass before calling an interface ready.", color = palette.muted, fontSize = (12f * textScale).sp)
    Spacer(Modifier.height(14.dp))
    Surface(color = palette.secondary, shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Review progress", color = palette.ink, fontSize = (14f * textScale).sp, fontWeight = FontWeight.Bold)
                Text(checks.count { it }.toString() + " of " + checks.size + " checks complete", color = palette.muted, fontSize = (11f * textScale).sp)
            }
            Text((checks.count { it } * 100 / checks.size).toString() + "%", color = palette.primary, fontSize = (21f * textScale).sp, fontWeight = FontWeight.Bold)
        }
    }
    Spacer(Modifier.height(13.dp))
    val items = listOf(
        "Navigate without a pointer" to "Can you reach every action with a keyboard?",
        "Listen to the screen" to "Do labels and reading order make sense?",
        "Increase text size" to "Does the layout still work when text grows?",
        "Check color contrast" to "Can you read content without relying on color alone?",
        "Try a real touch" to "Are controls comfortable and easy to activate?",
    )
    items.forEachIndexed { index, item ->
        val isChecked = checks[index]
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(17.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                .clickable { onToggle(index) }
                .semantics { contentDescription = item.first + if (isChecked) ", complete" else ", not complete" },
        ) {
            Row(Modifier.padding(horizontal = 14.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = if (isChecked) palette.primary else palette.secondary, shape = RoundedCornerShape(8.dp), modifier = Modifier.size(24.dp)) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (isChecked) Text("✓", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(item.first, color = palette.ink, fontSize = (12f * textScale).sp, fontWeight = FontWeight.SemiBold)
                    Text(item.second, color = palette.muted, fontSize = (10f * textScale).sp, lineHeight = (15f * textScale).sp)
                }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
    InfoCard("Keep learning", "Ask people who use it", "A checklist is a starting point; include disabled people in product reviews.", palette)
}

@Composable
private fun PreferenceRow(
    title: String,
    detail: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    palette: LensPalette,
    largeText: Boolean,
) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, color = palette.ink, fontSize = (13f * scale(largeText)).sp, fontWeight = FontWeight.SemiBold)
            Text(detail, color = palette.muted, fontSize = (10f * scale(largeText)).sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.semantics { contentDescription = "$title setting" },
        )
    }
}

@Composable
private fun ThresholdRow(label: String, threshold: String, passes: Boolean, palette: LensPalette, largeText: Boolean) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(15.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(label, color = palette.ink, fontSize = (12f * scale(largeText)).sp, fontWeight = FontWeight.SemiBold)
                Text("Threshold: $threshold", color = palette.muted, fontSize = (10f * scale(largeText)).sp)
            }
            StatusBadge(if (passes) "PASS" else "CHECK", passes, palette)
        }
    }
}

@Composable
private fun StatusLine(label: String, passes: Boolean, palette: LensPalette, largeText: Boolean) {
    val foreground = if (passes) Color(0xFF1F6A4C) else Color(0xFF8D4038)
    val background = if (passes) Color(0xFFE1F4EA) else Color(0xFFFBE7E3)
    Surface(color = background, shape = RoundedCornerShape(13.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 11.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (passes) "✓" else "!", color = foreground, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(8.dp))
            Text(label, color = foreground, fontSize = (11f * scale(largeText)).sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun StatusBadge(label: String, passes: Boolean, palette: LensPalette) {
    val foreground = if (passes) Color(0xFF1F6A4C) else Color(0xFF8D4038)
    val background = if (passes) Color(0xFFE1F4EA) else Color(0xFFFBE7E3)
    Surface(color = background, shape = CircleShape) {
        Text(label, Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = foreground, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
    }
}

@Composable
private fun ChoiceChip(label: String, selected: Boolean, palette: LensPalette, onClick: () -> Unit) {
    Surface(
        color = if (selected) palette.primary else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Text(label, Modifier.padding(horizontal = 13.dp, vertical = 11.dp), color = if (selected) Color.White else palette.ink, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun BulletCard(title: String, detail: String, glyph: String, palette: LensPalette, largeText: Boolean) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = palette.secondary, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(38.dp)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(glyph, color = palette.primary, fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.width(11.dp))
            Column {
                Text(title, color = palette.ink, fontSize = (12f * scale(largeText)).sp, fontWeight = FontWeight.SemiBold)
                Text(detail, color = palette.muted, fontSize = (10f * scale(largeText)).sp)
            }
        }
    }
}

@Composable
private fun InfoCard(eyebrow: String, title: String, detail: String, palette: LensPalette) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = palette.secondary, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(40.dp)) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("i", color = palette.primary, fontSize = 17.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.width(11.dp))
            Column {
                Text(eyebrow.uppercase(), color = palette.primary, fontSize = 8.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold)
                Text(title, color = palette.ink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(detail, color = palette.muted, fontSize = 10.sp, lineHeight = 15.sp)
            }
        }
    }
}

@Composable
private fun SectionHeading(title: String, eyebrow: String, palette: LensPalette, largeText: Boolean) {
    Column {
        Eyebrow(eyebrow, palette)
        Spacer(Modifier.height(4.dp))
        Text(title, color = palette.ink, fontSize = (17f * scale(largeText)).sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Eyebrow(text: String, palette: LensPalette) {
    Text(text, color = palette.primary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.35.sp)
}

@Composable
private fun Title(text: String, palette: LensPalette, largeText: Boolean) {
    Text(text, color = palette.ink, fontSize = (24f * scale(largeText)).sp, lineHeight = (29f * scale(largeText)).sp, fontWeight = FontWeight.Bold)
}

private fun scale(largeText: Boolean): Float = if (largeText) 1.22f else 1f
