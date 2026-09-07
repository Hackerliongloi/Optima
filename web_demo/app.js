// iQOO AI Phone Doctor - Web Prototype Application Logic

const SCENARIOS = {
    BATTERY_DRAIN: {
        title: "Battery Drain",
        subtitle: "Unusual background activity & network strain",
        healthScore: 87,
        issueTitle: "⚠️ Abnormal Battery Drain",
        issueDesc: "Your battery is draining approximately 28% faster than your normal pattern.",
        causes: [
            { name: "Background App Activity (Instagram)", percent: 62, icon: "📱" },
            { name: "High Temperature", percent: 24, icon: "🌡️" },
            { name: "Poor Network Signal", percent: 14, icon: "📶" }
        ],
        explanation: "Instagram has been using unusually high background activity while your device temperature is elevated. Poor network conditions may also be increasing power consumption.",
        recommendation: "Restrict Instagram background activity.",
        recReason: "This should reduce unnecessary background processing and help lower battery consumption.",
        telemetryBefore: { temp: 43, drain: 8.2, cpu: 78, ram: 82, storage: 68, network: "Poor", app: "Instagram" },
        telemetryAfter: { temp: 39, drain: 6.7, cpu: 61, ram: 69, storage: 68, network: "Good", app: "Instagram (Restricted)" }
    },
    THERMAL_STRESS: {
        title: "Thermal Stress",
        subtitle: "Gaming while charging + high CPU load",
        healthScore: 74,
        issueTitle: "⚠️ Thermal Stress Warning",
        issueDesc: "Your device temperature is rising unusually quickly during active usage.",
        causes: [
            { name: "Gaming Workload", percent: 55, icon: "🎮" },
            { name: "Fast Battery Charging", percent: 30, icon: "⚡" },
            { name: "Background Processing", percent: 15, icon: "⚙️" }
        ],
        explanation: "High CPU gaming demands coupled with fast charging are generating compound heat. Heavy background tasks are compounding thermal accumulation.",
        recommendation: "Switch to Balanced Thermal Profile.",
        recReason: "Reduces peak CPU heat spike and regulates charging power to quickly lower temperature.",
        telemetryBefore: { temp: 46.5, drain: 11.4, cpu: 91, ram: 86, storage: 64, network: "Good", app: "Gaming App" },
        telemetryAfter: { temp: 40.2, drain: 7.8, cpu: 68, ram: 71, storage: 64, network: "Good", app: "Gaming App (Balanced)" }
    },
    STORAGE_PRESSURE: {
        title: "Storage Pressure",
        subtitle: "System cache & duplicate media accumulation",
        healthScore: 79,
        issueTitle: "💾 Storage Pressure Warning",
        issueDesc: "Internal storage is at 94% capacity, causing system RAM swap slowdowns.",
        causes: [
            { name: "App & System Caches", percent: 52, icon: "🗑️" },
            { name: "Duplicate Photos & Videos", percent: 33, icon: "🖼️" },
            { name: "Unused Download Packages", percent: 15, icon: "📦" }
        ],
        explanation: "System cache accumulation has depleted free swap space, slowing down multitasking transitions and app launches.",
        recommendation: "Purge 14.2 GB of Temporary Caches.",
        recReason: "Frees up swap space immediately and restores original system responsiveness.",
        telemetryBefore: { temp: 37, drain: 6.9, cpu: 54, ram: 88, storage: 94, network: "Excellent", app: "Gallery" },
        telemetryAfter: { temp: 35.5, drain: 6.2, cpu: 42, ram: 64, storage: 78, network: "Excellent", app: "Gallery" }
    },
    GAMING_PERFORMANCE: {
        title: "Gaming Performance",
        subtitle: "Predicted thermal throttling during gaming session",
        healthScore: 83,
        issueTitle: "🎮 Gaming Performance Alert",
        issueDesc: "Predicted thermal throttling event during current gaming session.",
        causes: [
            { name: "Sustained GPU Load", percent: 58, icon: "🎮" },
            { name: "Background Network Sync", percent: 27, icon: "📡" },
            { name: "High Display Brightness", percent: 15, icon: "☀️" }
        ],
        explanation: "Based on current thermal trend, gaming performance may decline within approximately 9 minutes.",
        recommendation: "Activate iQOO Game Ultra Balanced Mode.",
        recReason: "Smooths out FPS spikes, caps thermal generation, and prevents thermal throttling.",
        telemetryBefore: { temp: 41, drain: 9.8, cpu: 85, ram: 80, storage: 60, network: "Good", app: "Gaming App" },
        telemetryAfter: { temp: 37.8, drain: 7.1, cpu: 65, ram: 68, storage: 60, network: "Good", app: "Gaming App (Optimized)" }
    }
};

let currentTab = 'home';
let currentScenarioKey = 'BATTERY_DRAIN';
let appState = 'INITIAL'; // INITIAL, SCANNING, DIAGNOSED, FIXING, VERIFIED
let historyLog = [
    {
        title: "Battery Drain Detected",
        severity: "RED",
        time: "Today 10:42 AM",
        cause: "Background app activity (Instagram)",
        action: "Background activity restricted",
        result: "Fixed (-1.5%/hr drain)"
    },
    {
        title: "Thermal Stress Warning",
        severity: "YELLOW",
        time: "Yesterday 08:15 PM",
        cause: "Gaming + fast charging",
        action: "Balanced performance profile active",
        result: "Fixed (-6.3°C lowered)"
    }
];

let chatMessages = [
    {
        sender: "AI",
        text: "Hello! I am your AI Phone Doctor. I continuously monitor your iQOO device telemetry to detect thermal stress, battery drain, and memory pressure. How can I help you today?",
        sources: []
    }
];

let geminiApiKey = "";
let storageCleaned = false;
let virusScanned = false;
let appsRestricted = false;

document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    setupJudgeControls();
    renderScreen();
});

function setupNavigation() {
    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.nav-item').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            currentTab = btn.dataset.tab;
            renderScreen();
        });
    });
}

function setupJudgeControls() {
    document.querySelectorAll('.btn-scenario').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.btn-scenario').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            currentScenarioKey = btn.dataset.scenario;
            appState = 'INITIAL';
            renderScreen();
        });
    });

    const saveKeyBtn = document.getElementById('saveKeyBtn');
    if (saveKeyBtn) {
        saveKeyBtn.addEventListener('click', () => {
            geminiApiKey = document.getElementById('geminiKey').value.trim();
            alert(geminiApiKey ? "Gemini API Key Saved!" : "Gemini API Key Cleared (Using Offline Engine)");
        });
    }
}

function renderScreen() {
    const container = document.getElementById('mainContent');
    const scenario = SCENARIOS[currentScenarioKey];

    // Sync navigation bar active state
    document.querySelectorAll('.nav-item').forEach(b => {
        if (b.dataset.tab === currentTab) b.classList.add('active');
        else b.classList.remove('active');
    });

    if (currentTab === 'home') {
        renderHomeScreen(container, scenario);
    } else if (currentTab === 'diagnose') {
        renderDiagnoseScreen(container, scenario);
    } else if (currentTab === 'chat') {
        renderChatScreen(container, scenario);
    } else if (currentTab === 'history') {
        renderHistoryScreen(container);
    } else if (currentTab === 'profile') {
        renderProfileScreen(container);
    }
}

// ----------------------------------------------------
// SCREEN RENDERING FUNCTIONS
// ----------------------------------------------------

function renderHomeScreen(container, scenario) {
    const isFixed = appState === 'VERIFIED';
    const isDiagnosed = appState === 'DIAGNOSED' || appState === 'VERIFIED';

    const tel = isFixed ? scenario.telemetryAfter : scenario.telemetryBefore;
    const score = isFixed ? 98 : scenario.healthScore;

    const scoreCircleContent = isDiagnosed ? `
        <div class="score-num">${score}</div>
        <div class="score-max">pts</div>
    ` : `
        <div style="font-size: 38px; line-height: 1;">🩺</div>
        <div class="score-max" style="margin-top: 4px; font-weight: 800; color: #30D158;">READY</div>
    `;

    const conditionText = !isDiagnosed && !isFixed
        ? 'Tap Diagnose to scan real-time phone health & detect issues.'
        : (isFixed ? '✓ All issues fixed. System is in good condition.' : '⚠️ Issues detected. Optimization recommended.');

    const buttonText = isFixed ? 'Re-scan Device' : (isDiagnosed ? 'Optimise' : 'Diagnose');

    container.innerHTML = `
        <!-- Top App Bar Inspired by Phone Manager Screenshot -->
        <div style="display: flex; justify-content: space-between; align-items: center; padding: 4px 2px 8px 2px;">
            <div style="font-size: 24px; font-weight: 800; color: #FFFFFF; font-family: var(--font-heading);">Optima</div>
            <div style="font-size: 20px; color: #9BA49E; cursor: pointer;">⋮</div>
        </div>

        <!-- Circular Score Gauge -->
        <div class="health-score-container">
            <div class="score-circle">
                <div class="score-circle-inner">
                    ${scoreCircleContent}
                </div>
            </div>
            <div class="system-condition-text">
                ${conditionText}
            </div>
            <button class="btn-primary" onclick="startScanSequence()">
                ${buttonText}
            </button>
        </div>

        <!-- AI Assistant Banner (Positioned right below Optimise section) -->
        <div class="card" style="border-color: rgba(48, 209, 88, 0.45); cursor: pointer;" onclick="currentTab='chat'; renderScreen();">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div style="display: flex; gap: 12px; align-items: center;">
                    <img src="app_logo.png" alt="Logo" style="width: 38px; height: 38px; border-radius: 10px; object-fit: cover; border: 1.5px solid #30D158;">
                    <div>
                        <div style="font-size: 10px; font-weight: 800; color: #30D158; font-family: var(--font-heading); letter-spacing: 0.5px;">ASK OPTIMA AI</div>
                        <div style="font-size: 14px; font-weight: 700; color: var(--text-primary); margin-top: 2px;">Wanna talk about your phone issue?</div>
                    </div>
                </div>
                <span style="background: rgba(48, 209, 88, 0.15); color: #30D158; padding: 6px 12px; border-radius: 10px; font-size: 12px; font-weight: 800; font-family: var(--font-heading);">Chat →</span>
            </div>
        </div>

        <!-- Gaming Health Card (Positioned below AI Assistant Banner) -->
        <div class="card">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
                <span style="font-size: 15px; font-weight: 800; font-family: var(--font-heading); display: flex; align-items: center; gap: 6px;">
                    <span>🎮</span> Gaming Health
                </span>
                <span style="background: ${isFixed ? 'rgba(48, 209, 88, 0.15)' : 'rgba(255, 214, 10, 0.15)'}; color: ${isFixed ? '#30D158' : '#FFD60A'}; padding: 3px 9px; border-radius: 6px; font-size: 11px; font-weight: 800; font-family: var(--font-heading);">
                    ${isFixed ? '60 FPS' : '54 FPS'}
                </span>
            </div>
            <div style="display: grid; grid-template-columns: repeat(4, 1fr); gap: 6px; font-size: 12px; text-align: center;">
                <div>
                    <div style="color: var(--text-muted); font-size: 10px;">Temp</div>
                    <div style="font-weight: 800; font-family: var(--font-heading); margin-top: 2px;">${tel.temp}°C</div>
                </div>
                <div>
                    <div style="color: var(--text-muted); font-size: 10px;">RAM</div>
                    <div style="font-weight: 800; font-family: var(--font-heading); margin-top: 2px;">${tel.ram}%</div>
                </div>
                <div>
                    <div style="color: var(--text-muted); font-size: 10px;">Drain</div>
                    <div style="font-weight: 800; font-family: var(--font-heading); margin-top: 2px;">${tel.drain}%/hr</div>
                </div>
                <div>
                    <div style="color: var(--text-muted); font-size: 10px;">Stability</div>
                    <div style="font-weight: 800; font-family: var(--font-heading); margin-top: 2px;">${isFixed ? '99%' : '91%'}</div>
                </div>
            </div>
            <div style="font-size: 11.5px; color: ${isFixed ? '#30D158' : '#FFD60A'}; margin-top: 10px; font-weight: 500;">
                ${isFixed ? '✓ Gaming profile optimized for maximum stability.' : '⚠️ Performance may decrease if temperature continues rising.'}
            </div>
        </div>

        <!-- 2x2 Main Grid Cards (Storage, Risks, System Boost, App Management) -->
        <div class="pm-grid">
            <div class="pm-card" onclick="openStorageModal()" style="cursor: pointer;">
                <div>
                    <div class="pm-card-icon">🧹</div>
                    <div class="pm-card-title">Storage cleanup</div>
                </div>
                <div class="pm-card-sub">${storageCleaned ? '68 GB / 256 GB' : (tel.storage || 115) + ' GB / 256 GB'}</div>
            </div>

            <div class="pm-card" onclick="openVirusModal()" style="cursor: pointer;">
                <div>
                    <div class="pm-card-icon">🛡️</div>
                    <div class="pm-card-title">Viruses & risks</div>
                </div>
                <div class="pm-card-sub ${(virusScanned || isFixed) ? '' : 'red'}">
                    ${(virusScanned || isFixed) ? 'Clean & protected' : 'Manually scanned 5 days ago'}
                </div>
            </div>

            <div class="pm-card" onclick="executeSystemBoost()" style="cursor: pointer;">
                <div>
                    <div class="pm-card-icon">🚀</div>
                    <div class="pm-card-title">System boost</div>
                </div>
                <div class="pm-card-sub">Improve system performance.</div>
            </div>

            <div class="pm-card" onclick="openAppManagementModal()" style="cursor: pointer;">
                <div>
                    <div class="pm-card-icon">📱</div>
                    <div class="pm-card-title">App management</div>
                </div>
                <div class="pm-card-sub ${(appsRestricted || isFixed) ? '' : 'red'}">
                    ${(appsRestricted || isFixed) ? 'All apps optimized' : 'View weekly app usage'}
                </div>
            </div>
        </div>

        <!-- Device Health Telemetry (Battery, Thermal, Performance, Storage, Network) -->
        <div style="margin-top: 4px;">
            <div style="font-size: 15px; font-weight: 800; font-family: var(--font-heading); margin-bottom: 10px;">Device Health Telemetry</div>
            <div class="metrics-grid">
                <div class="metric-pill">
                    <div class="pill-header">
                        <span>🔋 Battery</span>
                        <span class="pill-status">Good</span>
                    </div>
                    <div class="pill-value">${tel.drain}% / hr</div>
                </div>
                <div class="metric-pill">
                    <div class="pill-header">
                        <span>🌡️ Thermal</span>
                        <span class="pill-status ${tel.temp > 42 ? 'warning' : ''}">${tel.temp > 42 ? 'Warning' : 'Good'}</span>
                    </div>
                    <div class="pill-value">${tel.temp}°C</div>
                </div>
                <div class="metric-pill">
                    <div class="pill-header">
                        <span>⚡ Performance</span>
                        <span class="pill-status">Good</span>
                    </div>
                    <div class="pill-value">${tel.cpu}% CPU</div>
                </div>
                <div class="metric-pill">
                    <div class="pill-header">
                        <span>💾 Storage</span>
                        <span class="pill-status ${tel.storage > 90 ? 'warning' : ''}">${tel.storage > 90 ? 'Warning' : 'Good'}</span>
                    </div>
                    <div class="pill-value">${tel.storage}%</div>
                </div>
            </div>
            <div class="metric-pill" style="margin-top: 10px;">
                <div class="pill-header">
                    <span>📶 Network</span>
                    <span class="pill-status ${tel.network === 'Poor' ? 'warning' : ''}">${tel.network === 'Poor' ? 'Fair' : 'Good'}</span>
                </div>
                <div class="pill-value">${tel.network || 'Good Quality'}</div>
            </div>
        </div>

        <!-- Protected Footer -->
        <div class="pm-protected-footer">
            <span>🛡️</span> Protected in real time by Optima
        </div>
    `;
}


function renderChatScreen(container, scenario) {
    const tel = scenario.telemetryBefore;

    let messagesHTML = chatMessages.map((msg, index) => {
        const isAI = msg.sender === 'AI';
        let sourcesHTML = '';
        if (isAI && msg.sources && msg.sources.length > 0) {
            let sourcesList = msg.sources.map(s => `<div>✓ ${s.label}: <strong>${s.val}</strong></div>`).join('');
            sourcesHTML = `
                <div class="rag-evidence-box">
                    <div style="font-size: 11px; font-weight: 800; color: var(--accent-cyan); margin-bottom: 4px; font-family: var(--font-heading);">
                        📊 Based on your phone data
                    </div>
                    <div style="font-size: 11.5px; color: var(--text-secondary); display: flex; flex-direction: column; gap: 2px;">
                        ${sourcesList}
                    </div>
                </div>
            `;
        }

        let actionBtn = '';
        if (isAI && msg.canFix) {
            actionBtn = `
                <button class="btn-primary" style="margin-top: 12px; height: 42px; font-size: 14px;" onclick="applyFixSequence()">
                    Fix Now
                </button>
            `;
        }

        return `
            <div class="chat-bubble ${isAI ? 'ai' : 'user'}">
                ${isAI ? `
                    <div style="display: flex; align-items: center; gap: 6px; margin-bottom: 6px;">
                        <img src="app_logo.png" style="width: 20px; height: 20px; border-radius: 5px; object-fit: cover;">
                        <span style="font-size: 11px; font-weight: 800; color: var(--accent-cyan); font-family: var(--font-heading);">AI Doctor</span>
                    </div>
                ` : ''}
                <div>${formatMarkdownHtml(msg.text)}</div>
                ${sourcesHTML}
                ${actionBtn}
            </div>
        `;
    }).join('');

    container.innerHTML = `
        <div style="position: sticky; top: 0; background: var(--bg-cyber); z-index: 10; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;">
            <div style="display: flex; align-items: center; gap: 10px;">
                <img src="app_logo.png" class="app-logo-avatar">
                <div style="font-size: 18px; font-weight: 800; font-family: var(--font-heading); color: var(--text-primary);">Optima AI Assistant</div>
            </div>
            <button style="background: none; border: none; color: var(--text-muted); font-size: 12px; cursor: pointer; font-weight: 600;" onclick="chatMessages=[chatMessages[0]]; renderScreen();">Clear</button>
        </div>

        <div class="chat-container">
            <div class="chat-messages" id="chatMsgContainer">
                ${messagesHTML}
            </div>

            <div class="suggested-chips">
                <button class="chip-btn" onclick="sendQuickQuery('Why is my phone lagging?')">Why is my phone lagging?</button>
                <button class="chip-btn" onclick="sendQuickQuery('Why is my battery draining so fast?')">Why is my battery draining?</button>
                <button class="chip-btn" onclick="sendQuickQuery('Why is my phone getting hot?')">Why is my phone hot?</button>
                <button class="chip-btn" onclick="sendQuickQuery('Why is the game dropping FPS?')">Why is the game dropping FPS?</button>
            </div>

            <div class="chat-input-bar">
                <input type="text" id="chatInput" placeholder="Ask AI Doctor about lag, battery, temp..." onkeypress="if(event.key==='Enter') sendUserMessage()">
                <button onclick="sendUserMessage()">➔</button>
            </div>
        </div>
    `;

    setTimeout(() => {
        const msgBox = document.getElementById('chatMsgContainer');
        if (msgBox) msgBox.scrollTop = msgBox.scrollHeight;
    }, 50);
}

function sendQuickQuery(query) {
    const input = document.getElementById('chatInput');
    if (input) input.value = query;
    sendUserMessage();
}

function sendUserMessage() {
    const input = document.getElementById('chatInput');
    if (!input || !input.value.trim()) return;

    const userText = input.value.trim();
    input.value = '';

    chatMessages.push({ sender: 'USER', text: userText });
    renderScreen();

    const scenario = SCENARIOS[currentScenarioKey];
    const tel = appState === 'VERIFIED' ? scenario.telemetryAfter : scenario.telemetryBefore;

    const queryLower = userText.toLowerCase();

    let sources = [
        { label: "Current RAM", val: `${tel.ram}%` },
        { label: "Temperature", val: `${tel.temp}°C` },
        { label: "Normal Temp Baseline", val: "36.0°C" },
        { label: "Active App", val: tel.app }
    ];

    if (queryLower.includes('battery') || queryLower.includes('drain')) {
        sources.push({ label: "Current Drain Rate", val: `${tel.drain}%/hr` });
        sources.push({ label: "Baseline Normal Drain", val: "6.4%/hr" });
    }

    if (geminiApiKey) {
        const ragPrompt = `
You are Optima AI Doctor, an expert device diagnostics assistant on an iQOO flagship smartphone.
Answer the user's question accurately using Retrieval-Augmented Generation (RAG) grounded on the live device telemetry below.

=== LIVE DEVICE TELEMETRY CONTEXT ===
- Device Model: iQOO 13 Flagship (16GB RAM)
- Active Foreground App: ${tel.app}
- Temperature: ${tel.temp}°C (Normal baseline ~36.0°C)
- Battery Drain Rate: ${tel.drain}%/hr (Normal baseline 6.4%/hr)
- RAM Usage: ${tel.ram}%
- Active Scenario: ${scenario.title}

=== USER QUESTION ===
"${userText}"

=== MANDATORY FORMAT RULES ===
Format your response clearly into 3 short sections using markdown:

🔍 **Root Cause**
(1-2 clear sentences explaining the issue using telemetry evidence: ${tel.temp}°C temp, ${tel.ram}% RAM, ${tel.drain}%/hr drain).

📊 **Live Telemetry Evidence**
• Temp: ${tel.temp}°C (Normal ~36°C)
• RAM Usage: ${tel.ram}%
• Drain Rate: ${tel.drain}%/hr
• Active App: ${tel.app}

🛠️ **Recommended Action**
(1-2 concise bullet points with actionable advice to fix the issue).
`;

        fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key=${geminiApiKey}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                contents: [{ parts: [{ text: ragPrompt }] }]
            })
        })
        .then(res => res.json())
        .then(data => {
            let aiText = "";
            try {
                if (data.candidates && data.candidates[0].content && data.candidates[0].content.parts[0].text) {
                    aiText = data.candidates[0].content.parts[0].text.trim();
                } else if (data.error) {
                    aiText = `⚠️ Gemini API Error: ${data.error.message || 'Check key'}.\n\n` + getOfflineRAGText(queryLower, tel);
                } else {
                    aiText = getOfflineRAGText(queryLower, tel);
                }
            } catch(e) {
                aiText = getOfflineRAGText(queryLower, tel);
            }
            chatMessages.push({ sender: 'AI', text: aiText, sources: sources, canFix: true });
            renderScreen();
        })
        .catch(err => {
            console.error("Gemini fetch error:", err);
            chatMessages.push({ sender: 'AI', text: getOfflineRAGText(queryLower, tel), sources: sources, canFix: true });
            renderScreen();
        });
    } else {
        setTimeout(() => {
            chatMessages.push({
                sender: 'AI',
                text: getOfflineRAGText(queryLower, tel),
                sources: sources,
                canFix: true
            });
            renderScreen();
        }, 600);
    }
}

function getOfflineRAGText(queryLower, tel) {
    let cause = "";
    if (queryLower.includes('battery') || queryLower.includes('drain')) {
        cause = `Your battery is draining at ${tel.drain}%/hr (28% higher than normal 6.4%/hr baseline) due to excessive background activity from ${tel.app}.`;
    } else if (queryLower.includes('hot') || queryLower.includes('heat') || queryLower.includes('warm')) {
        cause = `Device temperature sensors report ${tel.temp}°C (normal baseline ~36.0°C) caused by high CPU background workload.`;
    } else if (queryLower.includes('fps') || queryLower.includes('bgmi') || queryLower.includes('game')) {
        cause = `During your active ${tel.app} gaming session, thermal output reached ${tel.temp}°C with RAM at ${tel.ram}%, triggering frame drops.`;
    } else {
        cause = `Your phone is using ${tel.ram}% of available RAM and its temperature is ${tel.temp}°C (normal baseline is 36.0°C).`;
    }

    return `🔍 **Root Cause**\n${cause}\n\n📊 **Live Telemetry Evidence**\n• Device Temp: ${tel.temp}°C\n• RAM Usage: ${tel.ram}%\n• Battery Drain: ${tel.drain}%/hr\n• Active App: ${tel.app}\n\n🛠️ **Recommended Action**\n• Run one-tap device optimization to clear background memory pressure and restrict unneeded services.`;
}

function formatMarkdownHtml(text) {
    if (!text) return "";
    return text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/\n/g, '<br>');
}

function renderDiagnoseScreen(container, scenario) {
    if (appState === 'SCANNING') {
        container.innerHTML = `
            <div style="text-align: center; padding: 60px 10px;">
                <div style="font-size: 40px; margin-bottom: 20px; animation: spin 1s linear infinite;">⏳</div>
                <div style="font-size: 18px; font-weight: 800; font-family: var(--font-heading);" id="scanStepMsg">Analyzing battery drain patterns...</div>
                <div style="font-size: 12px; color: var(--text-secondary); margin-top: 8px;">Multi-signal AI correlation engine active...</div>
            </div>
        `;
        return;
    }

    if (appState === 'FIXING') {
        container.innerHTML = `
            <div style="text-align: center; padding: 60px 10px;">
                <div style="font-size: 40px; margin-bottom: 20px; color: var(--success-green);">⚙️</div>
                <div style="font-size: 18px; font-weight: 800; color: var(--success-green); font-family: var(--font-heading);">Applying recommended action...</div>
                <div style="font-size: 12px; color: var(--text-secondary); margin-top: 8px;">Monitoring phone telemetry & stabilization...</div>
            </div>
        `;
        return;
    }

    if (appState === 'VERIFIED') {
        renderVerificationScreen(container, scenario);
        return;
    }

    if (appState === 'DIAGNOSED') {
        renderDiagnosisResultScreen(container, scenario);
        return;
    }

    // Default Initial Screen
    container.innerHTML = `
        <div style="text-align: center; padding: 40px 10px;">
            <div style="font-size: 48px;">🩺</div>
            <div style="font-size: 22px; font-weight: 800; margin-top: 12px; font-family: var(--font-heading);">Ready to Diagnose</div>
            <div style="font-size: 13px; color: var(--text-secondary); margin-top: 6px;">Tap below to run multi-signal telemetry scan.</div>
            <button class="btn-primary" style="margin-top: 24px;" onclick="startScanSequence()">
                Start AI Scan
            </button>
        </div>
    `;
}

function renderDiagnosisResultScreen(container, scenario) {
    let causeHTML = scenario.causes.map(c => `
        <div class="cause-bar-item">
            <div class="cause-bar-label">
                <span>${c.icon} ${c.name}</span>
                <span style="font-weight: 800; color: var(--iqoo-orange); font-family: var(--font-heading);">${c.percent}%</span>
            </div>
            <div class="progress-track">
                <div class="progress-fill" style="width: ${c.percent}%;"></div>
            </div>
        </div>
    `).join('');

    container.innerHTML = `
        <div class="card" style="background: rgba(255, 59, 48, 0.12); border-color: rgba(255, 59, 48, 0.4);">
            <div style="font-size: 18px; font-weight: 800; color: var(--alert-red); font-family: var(--font-heading);">${scenario.issueTitle}</div>
            <div style="font-size: 13.5px; color: var(--text-primary); margin-top: 4px;">${scenario.issueDesc}</div>
        </div>

        <div class="card">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                <span style="font-size: 15px; font-weight: 800; font-family: var(--font-heading);">Likely Causes</span>
                <span class="card-header-tag">Correlated AI Analysis</span>
            </div>
            ${causeHTML}
        </div>

        <div class="card" style="border-color: rgba(0, 229, 255, 0.4); background: var(--dark-surface);">
            <div style="font-size: 14px; font-weight: 800; color: var(--accent-cyan); display: flex; align-items: center; gap: 6px; font-family: var(--font-heading);">
                <span>🧠</span> AI Explanation
            </div>
            <div style="font-size: 13.5px; margin-top: 6px; line-height: 1.45;">${scenario.explanation}</div>
        </div>

        <div class="card" style="border-color: rgba(255, 101, 0, 0.4);">
            <div class="card-header-tag">Recommended Action</div>
            <div style="font-size: 16px; font-weight: 800; margin-top: 4px; font-family: var(--font-heading);">${scenario.recommendation}</div>
            <div style="font-size: 12.5px; color: var(--text-secondary); margin-top: 4px;">${scenario.recReason}</div>

            <div style="display: flex; gap: 10px; margin-top: 16px;">
                <button class="btn-primary" style="flex: 1;" onclick="applyFixSequence()">Fix Now</button>
                <button class="btn-secondary" style="flex: 1;" onclick="appState='INITIAL'; renderScreen();">Not Now</button>
            </div>
        </div>
    `;
}

function renderVerificationScreen(container, scenario) {
    const before = scenario.telemetryBefore;
    const after = scenario.telemetryAfter;

    container.innerHTML = `
        <div class="card" style="background: rgba(48, 209, 88, 0.12); border-color: rgba(48, 209, 88, 0.5); text-align: center; padding: 20px;">
            <div style="font-size: 32px;">✓</div>
            <div style="font-size: 22px; font-weight: 900; color: var(--success-green); margin-top: 4px; font-family: var(--font-heading);">Optimization Successful</div>
            <div style="font-size: 12px; color: var(--text-secondary); margin-top: 2px;">Phone health restored & telemetry normalized</div>
        </div>

        <div style="font-size: 15px; font-weight: 800; font-family: var(--font-heading);">Before / After Telemetry Verification</div>

        <div class="comparison-grid">
            <div class="comp-card before">
                <div class="comp-title" style="color: var(--alert-red);">BEFORE FIX</div>
                <div class="comp-item">
                    <div class="comp-item-label">Temp</div>
                    <div class="comp-item-val" style="color: var(--alert-red);">${before.temp}°C</div>
                </div>
                <div class="comp-item">
                    <div class="comp-item-label">Drain</div>
                    <div class="comp-item-val" style="color: var(--alert-red);">${before.drain}% / hr</div>
                </div>
                <div class="comp-item">
                    <div class="comp-item-label">CPU</div>
                    <div class="comp-item-val" style="color: var(--alert-red);">${before.cpu}%</div>
                </div>
            </div>

            <div class="comp-card after">
                <div class="comp-title" style="color: var(--success-green);">AFTER FIX</div>
                <div class="comp-item">
                    <div class="comp-item-label">Temp</div>
                    <div class="comp-item-val" style="color: var(--success-green);">${after.temp}°C</div>
                </div>
                <div class="comp-item">
                    <div class="comp-item-label">Drain</div>
                    <div class="comp-item-val" style="color: var(--success-green);">${after.drain}% / hr</div>
                </div>
                <div class="comp-item">
                    <div class="comp-item-label">CPU</div>
                    <div class="comp-item-val" style="color: var(--success-green);">${after.cpu}%</div>
                </div>
            </div>
        </div>

        <div class="card" style="border-color: rgba(0, 229, 255, 0.4);">
            <div style="font-size: 13.5px; font-weight: 800; color: var(--accent-cyan); display: flex; align-items: center; gap: 6px; font-family: var(--font-heading);">
                <img src="app_logo.png" style="width: 20px; height: 20px; border-radius: 5px; object-fit: cover;">
                <span>AI Verification</span>
            </div>
            <div style="font-size: 13px; margin-top: 6px; line-height: 1.45;">
                Battery drain decreased by approximately 18% after the recommended intervention.
            </div>
            <div style="font-size: 12px; font-weight: 800; color: var(--success-green); margin-top: 10px; font-family: var(--font-heading);">
                ✓ Issue Fixed & Health Restored
            </div>
        </div>

        <div style="display: flex; gap: 10px; margin-top: 6px;">
            <button class="btn-primary" style="flex: 1;" onclick="currentTab='home'; renderScreen();">
                ← Go Back
            </button>
            <button class="btn-secondary" style="flex: 1; border-color: var(--iqoo-orange); color: var(--iqoo-orange);" onclick="currentTab='history'; renderScreen();">
                📜 View History Log
            </button>
        </div>
    `;
}

function renderHistoryScreen(container) {
    let itemsHTML = historyLog.map(item => `
        <div class="card">
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <span style="font-size: 15px; font-weight: 800; font-family: var(--font-heading); display: flex; align-items: center; gap: 6px;">
                    <span style="color: ${item.severity === 'RED' ? 'var(--alert-red)' : 'var(--warning-yellow)'}; font-size: 10px;">●</span>
                    ${item.title}
                </span>
                <span style="font-size: 11px; color: var(--text-muted);">${item.time}</span>
            </div>
            <div style="font-size: 12px; color: var(--text-secondary); margin-top: 8px;">
                <div><strong>Cause:</strong> ${item.cause}</div>
                <div><strong>Action:</strong> ${item.action}</div>
            </div>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 12px; font-size: 11px;">
                <span style="background: rgba(48, 209, 88, 0.15); color: var(--success-green); padding: 4px 8px; border-radius: 6px; font-weight: 700;">
                    Result: ${item.result}
                </span>
            </div>
        </div>
    `).join('');

    container.innerHTML = `
        <div style="font-size: 11px; font-weight: 800; color: var(--iqoo-orange); font-family: var(--font-heading); letter-spacing: 0.8px;">DIAGNOSTIC TIMELINE</div>
        <div style="font-size: 20px; font-weight: 800; font-family: var(--font-heading);">Diagnosis History</div>
        <div style="display: flex; flex-direction: column; gap: 12px; margin-top: 12px;">
            ${itemsHTML}
        </div>
    `;
}

function renderProfileScreen(container) {
    container.innerHTML = `
        <div style="font-size: 11px; font-weight: 800; color: var(--iqoo-orange); font-family: var(--font-heading); letter-spacing: 0.8px;">PERSONALIZATION BASELINE</div>
        <div style="font-size: 20px; font-weight: 800; font-family: var(--font-heading);">Device & User Profile</div>

        <div class="card" style="margin-top: 8px; border-color: rgba(255, 101, 0, 0.3);">
            <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 12px;">
                <img src="app_logo.png" style="width: 40px; height: 40px; border-radius: 10px; object-fit: cover; border: 1.5px solid var(--iqoo-orange);">
                <div>
                    <div style="font-size: 16px; font-weight: 800; font-family: var(--font-heading);">iQOO 13 Flagship</div>
                    <div style="font-size: 11px; color: var(--accent-cyan); font-weight: 600;">Personalized Baseline Profile</div>
                </div>
            </div>

            <div style="border-top: 1px solid var(--dark-border); padding-top: 10px; display: flex; flex-direction: column; gap: 8px; font-size: 12px;">
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">📱 Phone Model:</span>
                    <span style="font-weight: 700;">iQOO 13 (16GB RAM)</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">🎮 Usage Pattern:</span>
                    <span style="font-weight: 700;">Heavy Gaming & Multitasking</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">🔋 Battery Habit:</span>
                    <span style="font-weight: 700;">Frequent Fast Charging</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">⚡ Performance Mode:</span>
                    <span style="font-weight: 700;">Balanced / Monster Mode</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">⏱️ Screen Time:</span>
                    <span style="font-weight: 700;">6.5 hours / day</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-secondary);">📊 Drain Baseline:</span>
                    <span style="font-weight: 800; color: var(--iqoo-orange); font-family: var(--font-heading);">6.4% / hour</span>
                </div>
            </div>

            <div style="background: var(--dark-surface); padding: 12px; border-radius: 12px; margin-top: 12px; font-size: 11.5px; color: var(--text-secondary); line-height: 1.45;">
                🧠 <strong>Why this matters:</strong> AI Phone Doctor compares your telemetry against this baseline. Today's 8.2%/hr drain is flagged because it is 28% above your normal pattern.
            </div>
        </div>

        <div class="card" style="background: var(--dark-surface);">
            <div style="font-size: 13.5px; font-weight: 800; font-family: var(--font-heading); display: flex; align-items: center; gap: 6px;">
                <span>🔒</span> On-Device Privacy Guard
            </div>
            <div style="font-size: 11.5px; color: var(--text-secondary); margin-top: 4px; line-height: 1.45;">
                All telemetry correlation occurs locally on your phone. No private logs or app data leave your device.
            </div>
        </div>
    `;
}

// ----------------------------------------------------
// SEQUENCES FOR SIMULATION FLOW
// ----------------------------------------------------

function startScanSequence() {
    currentTab = 'diagnose';
    appState = 'SCANNING';
    renderScreen();

    setTimeout(() => {
        appState = 'DIAGNOSED';
        renderScreen();
    }, 1800);
}

function applyFixSequence() {
    currentTab = 'diagnose';
    appState = 'FIXING';
    renderScreen();

    setTimeout(() => {
        appState = 'VERIFIED';

        const scenario = SCENARIOS[currentScenarioKey];
        historyLog.unshift({
            title: scenario.issueTitle.replace('⚠️ ', ''),
            severity: "RED",
            time: "Just Now",
            cause: scenario.causes[0].name,
            action: scenario.recommendation,
            result: "Fixed (-18% impact)"
        });

        renderScreen();
    }, 1600);
}

function runDiagnoseSequenceOnHome() {
    appState = 'SCANNING';
    renderScreen();
    setTimeout(() => {
        appState = 'DIAGNOSED';
        renderScreen();
    }, 1200);
}

function runOptimiseSequenceOnHome() {
    appState = 'FIXING';
    renderScreen();
    setTimeout(() => {
        appState = 'VERIFIED';
        const scenario = SCENARIOS[currentScenarioKey];
        historyLog.unshift({
            title: scenario.issueTitle.replace('⚠️ ', ''),
            severity: "GREEN",
            time: "Just Now",
            cause: scenario.causes[0].name,
            action: scenario.recommendation,
            result: "Fixed & Optimized"
        });
        renderScreen();
    }, 1400);
}

function resetHomeState() {
    appState = 'IDLE';
    renderScreen();
}

// ----------------------------------------------------
// 2x2 GRID CARD INTERACTIVE FEATURE HANDLERS & MODALS
// ----------------------------------------------------

function showToast(message) {
    const screen = document.querySelector('.phone-screen') || document.body;
    const existing = document.querySelector('.pm-toast');
    if (existing) existing.remove();

    const toast = document.createElement('div');
    toast.className = 'pm-toast';
    toast.innerHTML = message;
    screen.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transition = 'opacity 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 2200);
}

function closeModal() {
    const modal = document.querySelector('.pm-modal-overlay');
    if (modal) modal.remove();
}

function openStorageModal() {
    const screen = document.querySelector('.phone-screen') || document.body;
    closeModal();

    const overlay = document.createElement('div');
    overlay.className = 'pm-modal-overlay';
    overlay.innerHTML = `
        <div class="pm-modal-card">
            <div class="pm-modal-header">
                <div class="pm-modal-title"><span>🧹</span> Storage Cleanup</div>
                <button class="pm-modal-close" onclick="closeModal()">✕</button>
            </div>
            
            <div style="font-size: 13px; color: var(--text-secondary);">
                ${storageCleaned ? 'Storage optimized! Free space: 188 GB available.' : 'Deep analysis found 14.2 GB of safe-to-delete files.'}
            </div>

            <div style="background: rgba(255,255,255,0.04); border-radius: 16px; padding: 14px; display: flex; flex-direction: column; gap: 10px; font-size: 13px;">
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">🗑️ App & System Cache</span>
                    <span style="font-weight: 700;">3.4 GB</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">🖼️ Duplicate Media & Screenshots</span>
                    <span style="font-weight: 700;">8.0 GB</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="color: var(--text-muted);">📦 Residual Installation Files</span>
                    <span style="font-weight: 700;">2.8 GB</span>
                </div>
            </div>

            <div id="storageActionArea">
                ${storageCleaned ? `
                    <div style="text-align: center; color: #30D158; font-weight: 800; padding: 10px;">
                        ✓ Storage Cleaned (14.2 GB Released)
                    </div>
                ` : `
                    <button class="btn-primary" onclick="executeStorageCleanup()">
                        🧹 Clean 14.2 GB Junk
                    </button>
                `}
            </div>
        </div>
    `;
    screen.appendChild(overlay);
}

function executeStorageCleanup() {
    const area = document.getElementById('storageActionArea');
    if (area) {
        area.innerHTML = `
            <div style="text-align: center; font-size: 13px; color: #30D158; font-weight: 700; margin-bottom: 8px;">Purging cache & temporary files...</div>
            <div class="pm-progress-bar">
                <div class="pm-progress-fill" id="storageProg" style="width: 0%;"></div>
            </div>
        `;
    }

    setTimeout(() => {
        const fill = document.getElementById('storageProg');
        if (fill) fill.style.width = '100%';
    }, 100);

    setTimeout(() => {
        storageCleaned = true;
        closeModal();
        renderScreen();
        showToast("🧹 14.2 GB Storage Cleaned!");
    }, 1200);
}

function openVirusModal() {
    const screen = document.querySelector('.phone-screen') || document.body;
    closeModal();

    const overlay = document.createElement('div');
    overlay.className = 'pm-modal-overlay';
    overlay.innerHTML = `
        <div class="pm-modal-card">
            <div class="pm-modal-header">
                <div class="pm-modal-title"><span>🛡️</span> Security & Risk Scanner</div>
                <button class="pm-modal-close" onclick="closeModal()">✕</button>
            </div>

            <div style="font-size: 13px; color: var(--text-secondary);">
                ${virusScanned ? '✓ Real-time protection active. 0 threats detected.' : 'Scanning 124 app packages & system files for malware, scam links, and privacy risks.'}
            </div>

            <div id="virusActionArea">
                ${virusScanned ? `
                    <div style="background: rgba(48,209,88,0.12); border: 1px solid rgba(48,209,88,0.4); border-radius: 14px; padding: 14px; text-align: center; color: #30D158; font-weight: 800; font-size: 14px;">
                        ✓ Clean & Safe (0 Threats Found)
                    </div>
                ` : `
                    <button class="btn-primary" onclick="executeVirusScan()">
                        🛡️ Run Full Security Scan
                    </button>
                `}
            </div>
        </div>
    `;
    screen.appendChild(overlay);
}

function executeVirusScan() {
    const area = document.getElementById('virusActionArea');
    if (area) {
        area.innerHTML = `
            <div style="text-align: center; font-size: 13px; color: #30D158; font-weight: 700; margin-bottom: 8px;">Scanning 124 installed apps...</div>
            <div class="pm-progress-bar">
                <div class="pm-progress-fill" id="virusProg" style="width: 0%;"></div>
            </div>
        `;
    }

    setTimeout(() => {
        const fill = document.getElementById('virusProg');
        if (fill) fill.style.width = '100%';
    }, 100);

    setTimeout(() => {
        virusScanned = true;
        closeModal();
        renderScreen();
        showToast("🛡️ Security Scan Passed! 0 Threats Found.");
    }, 1300);
}

function executeSystemBoost() {
    showToast("🚀 System Boosted! Released 1.8 GB RAM.");
}

function openAppManagementModal() {
    const screen = document.querySelector('.phone-screen') || document.body;
    closeModal();

    const overlay = document.createElement('div');
    overlay.className = 'pm-modal-overlay';
    overlay.innerHTML = `
        <div class="pm-modal-card">
            <div class="pm-modal-header">
                <div class="pm-modal-title"><span>📱</span> App Management</div>
                <button class="pm-modal-close" onclick="closeModal()">✕</button>
            </div>

            <div style="font-size: 13px; color: var(--text-secondary);">
                Active background apps consuming power and memory:
            </div>

            <div style="display: flex; flex-direction: column; gap: 10px;">
                <div style="background: rgba(255,255,255,0.04); padding: 12px 14px; border-radius: 16px; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <div style="font-weight: 700; font-size: 14px; color: #FFFFFF;">Instagram</div>
                        <div style="font-size: 11px; color: #FF5252;">62% background drain</div>
                    </div>
                    <button class="btn-primary" style="width: auto; height: 34px; padding: 0 14px; font-size: 12px; border-radius: 12px;" onclick="executeAppRestrict('Instagram')">
                        ${appsRestricted ? 'Restricted ✓' : 'Restrict'}
                    </button>
                </div>

                <div style="background: rgba(255,255,255,0.04); padding: 12px 14px; border-radius: 16px; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <div style="font-weight: 700; font-size: 14px; color: #FFFFFF;">Gaming App</div>
                        <div style="font-size: 11px; color: #FFD60A;">24% thermal load</div>
                    </div>
                    <button class="btn-primary" style="width: auto; height: 34px; padding: 0 14px; font-size: 12px; border-radius: 12px;" onclick="executeAppRestrict('Gaming App')">
                        ${appsRestricted ? 'Optimized ✓' : 'Optimize'}
                    </button>
                </div>
            </div>
        </div>
    `;
    screen.appendChild(overlay);
}

function executeAppRestrict(appName) {
    appsRestricted = true;
    closeModal();
    renderScreen();
    showToast(`📱 ${appName} Background Power Restricted!`);
}

// Initial render
document.addEventListener('DOMContentLoaded', () => {
    renderScreen();
});

