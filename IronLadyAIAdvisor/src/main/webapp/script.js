const sendBtn = document.getElementById('sendBtn');
const userInput = document.getElementById('userInput');
const chatBox = document.getElementById('chatBox');
const restartBtn = document.getElementById('restartBtn');

// ---------- ADD MESSAGE ----------
function addMessage(text, type) {
    const msgDiv = document.createElement('div');
    msgDiv.classList.add('message', type);
    msgDiv.innerText = text;
    chatBox.appendChild(msgDiv);
    chatBox.scrollTop = chatBox.scrollHeight;
}

// ---------- ADD OPTION BUTTONS ----------
function addOptions(options) {
    // remove old option buttons first
    document.querySelectorAll('.options').forEach(o => o.remove());

    const optionDiv = document.createElement('div');
    optionDiv.classList.add('options');

    options.forEach(opt => {
        const btn = document.createElement('button');
        btn.innerText = opt.label;
        btn.onclick = () => {
            addMessage(opt.label, 'user-msg');
            sendMessage(opt.value);
            optionDiv.remove();
        };
        optionDiv.appendChild(btn);
    });

    chatBox.appendChild(optionDiv);
    chatBox.scrollTop = chatBox.scrollHeight;
}

// ---------- SEND MESSAGE ----------
function sendMessage(text) {
    fetch('chat', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `message=${encodeURIComponent(text)}`
    })
    .then(response => response.text())
    .then(reply => handleBotReply(reply))
    .catch(err => {
        addMessage("Server error. Please try again.", 'bot-msg');
        console.error(err);
    });
}

// ---------- HANDLE BOT REPLY ----------
function handleBotReply(reply) {
    addMessage(reply, 'bot-msg');

    const lowerReply = reply.toLowerCase();

    // ✅ FINAL PROGRAM FOUND → LOCK CHAT
    if (
        lowerReply.includes("target") &&
        lowerReply.includes("interest") &&
        lowerReply.includes("duration") &&
        lowerReply.includes("outcome")
    ) {
        userInput.disabled = true;
        sendBtn.disabled = true;

        // remove any remaining option buttons
        document.querySelectorAll('.options').forEach(o => o.remove());
        return;
    }

    // EDUCATION OPTIONS
    if (lowerReply.includes("education")) {
        addOptions([
            { label: "Student / Fresher", value: "STUDENT" },
            { label: "Graduate", value: "GRADUATE" },
            { label: "Working Professional", value: "WORKING_PRO" },
            { label: "Entrepreneur", value: "ENTREPRENEUR" },
            { label: "Senior Leader", value: "SENIOR_LEADER" }
        ]);
        userInput.disabled = true;
    }

    // INTEREST OPTIONS
    else if (lowerReply.includes("interest")) {
        addOptions([
            { label: "Career Clarity", value: "CAREER_CLARITY" },
            { label: "AI / Technology", value: "TECH" },
            { label: "Leadership", value: "LEADERSHIP" },
            { label: "Business", value: "BUSINESS" }
        ]);
        userInput.disabled = true;
    }

    // EXPERIENCE OPTIONS
    else if (lowerReply.includes("experience")) {
        addOptions([
            { label: "0–1 years", value: "ENTRY" },
            { label: "2–5 years", value: "MID" },
            { label: "6–10 years", value: "SENIOR" },
            { label: "10+ years", value: "EXEC" }
        ]);
        userInput.disabled = true;
    }

    // TARGET ROLE OPTIONS
    else if (lowerReply.includes("target")) {
        addOptions([
            { label: "Career Starter", value: "CAREER_STARTER" },
            { label: "Growth Seeker", value: "GROWTH_SEEKER" },
            { label: "Executive / CXO", value: "EXECUTIVE_ASPIRANT" },
            { label: "Entrepreneur", value: "ENTREPRENEUR" }
        ]);
        userInput.disabled = true;
    }

    // FREE TEXT INPUT
    else {
        userInput.disabled = false;
        sendBtn.disabled = false;
    }
}

// ---------- SEND BUTTON ----------
sendBtn.addEventListener('click', () => {
    const text = userInput.value.trim();
    if (!text) return;

    addMessage(text, 'user-msg');
    userInput.value = '';
    sendMessage(text);
});

// ---------- ENTER KEY ----------
userInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') sendBtn.click();
});

// ---------- RESTART CHAT ----------
restartBtn.addEventListener('click', () => {
    chatBox.innerHTML = "";
    userInput.disabled = false;
    sendBtn.disabled = false;
    userInput.value = "";

    fetch('chat', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'message=__RESTART__'
    })
    .then(res => res.text())
    .then(reply => addMessage(reply, 'bot-msg'));
});
