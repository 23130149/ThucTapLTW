(function () {
    function setupPasswordToggles() {
        document.querySelectorAll("input[type='password']").forEach(function (input) {
            if (input.dataset.passwordToggleReady === "true") return;

            const parent = input.parentElement;
            if (!parent) return;

            input.dataset.passwordToggleReady = "true";
            if (window.getComputedStyle(parent).position === "static") {
                parent.style.position = "relative";
            }

            input.style.paddingRight = "48px";

            const existingIcon = Array.from(parent.children).find(function (child) {
                return child !== input && child.tagName === "I";
            });
            if (existingIcon) {
                existingIcon.style.right = "48px";
            }

            const button = document.createElement("button");
            button.type = "button";
            button.className = "password-visibility-toggle";
            button.setAttribute("aria-label", "Hiện mật khẩu");
            button.innerHTML = "<i class='bx bx-show'></i>";

            button.addEventListener("click", function () {
                const showing = input.type === "text";
                input.type = showing ? "password" : "text";
                button.setAttribute("aria-label", showing ? "Hiện mật khẩu" : "Ẩn mật khẩu");
                button.innerHTML = showing
                    ? "<i class='bx bx-show'></i>"
                    : "<i class='bx bx-hide'></i>";
            });

            parent.appendChild(button);
        });
    }

    const style = document.createElement("style");
    style.textContent = [
        ".password-visibility-toggle{position:absolute;right:12px;top:50%;transform:translateY(-50%);",
        "width:32px;height:32px;border:0;background:transparent;color:#475467;display:inline-flex;",
        "align-items:center;justify-content:center;cursor:pointer;z-index:4;padding:0}",
        ".password-visibility-toggle i{position:static!important;transform:none!important;font-size:20px!important}",
        ".password-visibility-toggle:hover{color:#11998e}"
    ].join("");
    document.head.appendChild(style);

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", setupPasswordToggles);
    } else {
        setupPasswordToggles();
    }
})();
