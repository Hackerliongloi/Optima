package com.iqoo.aiphonedoctor.ui.screens

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqoo.aiphonedoctor.R
import com.iqoo.aiphonedoctor.data.model.ChatMessage
import com.iqoo.aiphonedoctor.ui.theme.*
import com.iqoo.aiphonedoctor.ui.viewmodel.MainViewModel

@Preview(showBackground = true)
@Composable
fun AIDoctorChatScreenPreview() {
    AIPhoneDoctorTheme {
        AIDoctorChatScreen(viewModel = MainViewModel(Application()))
    }
}

@Composable
fun AIDoctorChatScreen(viewModel: MainViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val isTyping by viewModel.isChatTyping.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp)
    ) {
        // Sticky Header: Optima Logo + "Optima AI Assistant"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Optima Logo",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .border(1.dp, IqooOrange, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Optima AI Assistant",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            IconButton(onClick = { viewModel.clearChat() }) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear Chat",
                    tint = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                ChatMessageBubble(msg = msg, onTriggerFix = { viewModel.triggerFixFromChat() })
            }

            if (isTyping) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarkCardBg)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = AccentCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Doctor is inspecting phone telemetry & knowledge base...",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Suggested Questions Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.suggestedQuestions) { question ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(DarkCardBg)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(18.dp))
                        .clickable { viewModel.sendChatMessage(question) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = question,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = AccentCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Box
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask AI Doctor about lag, temp, battery...", color = TextMuted, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IqooOrange,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkCardBg,
                    unfocusedContainerColor = DarkCardBg
                )
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendChatMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(IqooOrange)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = CyberBlack
                )
            }
        }
    }
}

@Composable
fun ChatMessageBubble(msg: ChatMessage, onTriggerFix: () -> Unit) {
    val isUser = msg.sender == "USER"
    var showSources by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 310.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) IqooOrange.copy(alpha = 0.2f) else DarkCardBg
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = BorderStroke(
                1.dp,
                if (isUser) IqooOrange.copy(alpha = 0.5f) else DarkCardBorder
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "Optima Logo",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Optima AI Doctor",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentCyan
                            )
                        }
                        Text(
                            text = msg.timestamp,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Formatted Text Rendering (no raw asterisks, strictly line-by-line)
                Text(
                    text = buildMarkdownAnnotatedString(msg.text),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextPrimary
                )

                // Expandable RAG Telemetry Context Badge
                if (!isUser && msg.ragSources.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface)
                            .border(1.dp, AccentCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .clickable { showSources = !showSources }
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📊 Telemetry Grounded Context",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentCyan
                                )
                                Text(
                                    text = if (showSources) "▲" else "▼",
                                    fontSize = 10.sp,
                                    color = AccentCyan
                                )
                            }

                            if (showSources) {
                                Spacer(modifier = Modifier.height(6.dp))
                                msg.ragSources.forEach { source ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 1.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "✓ ${source.title}", fontSize = 11.sp, color = TextSecondary)
                                        Text(text = source.value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                // Interactive "Fix Now" Action Button inside AI Chat Bubble
                if (!isUser && msg.canTriggerFix) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onTriggerFix,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IqooOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Fix Now",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberBlack
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom AnnotatedString parser that renders **bold** text without showing raw asterisks
 * and strictly preserves line-by-line (\n) bullet point structure.
 */
fun buildMarkdownAnnotatedString(text: String): AnnotatedString {
    val lines = text.lines().joinToString("\n") { line ->
        val trimmed = line.trimStart()
        if (trimmed.startsWith("#")) {
            trimmed.replace(Regex("^#+\\s*"), "")
        } else {
            line
        }
    }

    return buildAnnotatedString {
        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
        var lastIndex = 0
        val matches = boldRegex.findAll(lines)

        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1

            if (start > lastIndex) {
                append(lines.substring(lastIndex, start))
            }

            val boldContent = match.groupValues[1]
            val boldStart = length
            append(boldContent)
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold, color = TextPrimary),
                start = boldStart,
                end = boldStart + boldContent.length
            )

            lastIndex = end
        }

        if (lastIndex < lines.length) {
            append(lines.substring(lastIndex))
        }
    }
}
