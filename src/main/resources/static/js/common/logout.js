function getCookieValue(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length !== 2) {
        return null;
    }
    return parts.pop().split(";").shift();
}

async function handleLogoutClick(button) {
    const logoutUrl = button.dataset.logoutUrl;
    const loginUrl = button.dataset.loginUrl || "/login?logout=1";
    const errorMessage = button.dataset.errorMessage || "Sign out failed.";
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute("content") || "X-XSRF-TOKEN";
    const csrfToken = getCookieValue("XSRF-TOKEN") || document.querySelector('meta[name="_csrf"]')?.getAttribute("content");

    if (!logoutUrl || !csrfToken) {
        window.alert(errorMessage);
        return;
    }

    button.disabled = true;

    try {
        const headers = {
            "X-XSRF-TOKEN": csrfToken
        };

        if (csrfHeader && csrfHeader !== "X-XSRF-TOKEN") {
            headers[csrfHeader] = csrfToken;
        }

        const response = await fetch(logoutUrl, {
            method: "POST",
            credentials: "same-origin",
            headers
        });

        if (!response.ok) {
            throw new Error(`Logout failed with status ${response.status}`);
        }

        window.location.assign(loginUrl);
    } catch (error) {
        console.error("Logout request failed", error);
        window.alert(errorMessage);
        button.disabled = false;
    }
}

document.querySelectorAll(".logout-button[data-logout-url]").forEach((button) => {
    button.addEventListener("click", () => handleLogoutClick(button));
});
