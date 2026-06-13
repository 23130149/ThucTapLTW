(function () {
    if (window.__ajaxEnhanceLoaded) return;
    window.__ajaxEnhanceLoaded = true;

    const contextPath = window.APP_CONTEXT || document.body.getAttribute("data-context-path") || "";

    function injectAjaxStyle() {
        if (document.getElementById("ajax-enhance-style")) return;
        const style = document.createElement("style");
        style.id = "ajax-enhance-style";
        style.textContent = ".ajax-message{position:fixed;right:22px;bottom:22px;z-index:9999;max-width:min(360px,calc(100vw - 32px));padding:13px 16px;border-radius:14px;background:#17363a;color:#fff;font-size:14px;font-weight:800;box-shadow:0 18px 40px rgba(15,23,42,.22);opacity:0;transform:translateY(12px);pointer-events:none;transition:opacity .18s ease,transform .18s ease}.ajax-message.show{opacity:1;transform:translateY(0)}.ajax-message.success{background:#11998e}.ajax-message.error{background:#d93025}";
        document.head.appendChild(style);
    }

    function formatMoney(value) {
        return new Intl.NumberFormat("vi-VN").format(Number(value || 0)) + " đ";
    }

    function showAjaxMessage(message, success) {
        if (!message) return;

        let box = document.querySelector(".ajax-message");
        if (!box) {
            box = document.createElement("div");
            box.className = "ajax-message";
            document.body.appendChild(box);
        }

        box.textContent = message;
        box.className = "ajax-message " + (success ? "success" : "error") + " show";
        window.clearTimeout(box._timer);
        box._timer = window.setTimeout(function () {
            box.classList.remove("show");
        }, 2200);
    }

    function openLoginModalIfPresent(event) {
        const modal = document.getElementById("hhLoginModal");
        if (!modal) return false;

        if (event) {
            event.preventDefault();
            event.stopImmediatePropagation();
        }

        const loginLink = modal.querySelector(".hh-login-button");
        if (loginLink) {
            let current = window.location.pathname + window.location.search;
            if (contextPath && current.startsWith(contextPath)) {
                current = current.substring(contextPath.length);
            }
            current = current.replace(/^\/+/, "") || "home";
            loginLink.href = contextPath + "/SignIn?redirect=" + encodeURIComponent(current);
        }

        modal.classList.add("show");
        modal.setAttribute("aria-hidden", "false");
        return true;
    }

    async function requestJson(url, options) {
        const fetchOptions = Object.assign({}, options || {});

        // Ép backend nhận diện đây là Ajax bằng cả header lẫn tham số ajax=1.
        // Một số case redirect/filter/error có thể làm header không đủ tin cậy,
        // nên tham số này giúp controller AjaxUtil.wantsJson(request) luôn bắt đúng.
        let requestUrl = url;
        const method = (fetchOptions.method || "GET").toUpperCase();

        if (fetchOptions.body instanceof FormData) {
            if (!fetchOptions.body.has("ajax")) {
                fetchOptions.body.append("ajax", "1");
            }
        } else if (fetchOptions.body instanceof URLSearchParams) {
            fetchOptions.body.set("ajax", "1");
        } else if (method === "GET" || !fetchOptions.body) {
            const separator = requestUrl.indexOf("?") === -1 ? "?" : "&";
            if (!/[?&]ajax=/.test(requestUrl)) {
                requestUrl += separator + "ajax=1";
            }
        }

        fetchOptions.headers = Object.assign({}, fetchOptions.headers || {}, {
            "Accept": "application/json",
            "X-Requested-With": "XMLHttpRequest",
            "Cache-Control": "no-cache"
        });
        fetchOptions.credentials = "same-origin";

        let response;
        let text;
        try {
            response = await fetch(requestUrl, fetchOptions);
            text = await response.text();
        } catch (error) {
            return {
                success: false,
                message: "Không thể kết nối máy chủ. Vui lòng thử lại.",
                detail: error && error.message ? error.message : ""
            };
        }

        const contentType = response.headers.get("content-type") || "";

        if (response.redirected) {
            return {
                success: false,
                message: "Phiên đăng nhập có thể đã hết hạn. Vui lòng đăng nhập lại rồi thử thao tác admin."
            };
        }

        if (!contentType.includes("application/json")) {
            const cleanText = text.replace(/<script[\s\S]*?<\/script>/gi, " ")
                .replace(/<style[\s\S]*?<\/style>/gi, " ")
                .replace(/<[^>]+>/g, " ")
                .replace(/\s+/g, " ")
                .trim();

            return {
                success: false,
                message: response.status === 401 || response.status === 403
                    ? "Bạn chưa đăng nhập admin hoặc không đủ quyền thực hiện thao tác này."
                    : "Máy chủ trả về HTML thay vì JSON. Kiểm tra log Tomcat để xem lỗi gốc.",
                detail: cleanText.substring(0, 220)
            };
        }

        try {
            const data = JSON.parse(text);
            if (!response.ok && data.success !== false) {
                data.success = false;
            }
            return data;
        } catch (error) {
            return {success: false, message: "Máy chủ trả JSON bị lỗi định dạng."};
        }
    }

    function formRequest(form) {
        const method = (form.method || "GET").toUpperCase();
        const data = new FormData(form);
        const actionUrl = form.getAttribute("action") || window.location.href;

        if (method === "GET") {
            const params = new URLSearchParams(data).toString();
            const separator = actionUrl.indexOf("?") === -1 ? "?" : "&";
            return {
                url: actionUrl + (params ? separator + params : ""),
                options: {method: "GET"}
            };
        }

        return {
            url: actionUrl,
            options: {
                method: method,
                body: (form.enctype || "").toLowerCase() === "multipart/form-data"
                    ? data
                    : new URLSearchParams(data)
            }
        };
    }

    function updateCartSummary(cart) {
        if (!cart) return;

        document.querySelectorAll(".hh-action[href$='/cart']").forEach(function (link) {
            let badge = link.querySelector(".hh-cart-count");
            const quantity = Number(cart.totalQuantity || 0);

            if (quantity <= 0) {
                if (badge) badge.remove();
                return;
            }

            if (!badge) {
                badge = document.createElement("span");
                badge.className = "hh-cart-count";
                link.appendChild(badge);
            }
            badge.textContent = quantity;
        });

        document.querySelectorAll("#selectedTotalTop, #selectedSubtotalBottom, #selectedTotalBottom")
            .forEach(function (node) {
                node.textContent = formatMoney(cart.totalPrice);
            });

        document.querySelectorAll("#selectedQuantityTop, #selectedQuantityBottom")
            .forEach(function (node) {
                node.textContent = cart.totalQuantity || 0;
            });
    }

    function refreshCartSelection() {
        const event = new Event("change");
        const firstCheckbox = document.querySelector(".item-checkbox");
        if (firstCheckbox) {
            firstCheckbox.dispatchEvent(event);
        } else {
            updateCartSummary({totalQuantity: 0, totalPrice: 0});
        }

        if (!document.querySelector(".cart-item")) {
            const page = document.querySelector(".cart-page");
            if (page) {
                page.innerHTML = "<h1 class=\"cart-header\"><i class='bx bx-cart'></i> Giỏ hàng của bạn</h1><p>Giỏ hàng của bạn đang trống</p><a href=\"" + contextPath + "/product\">Tiếp tục mua sắm</a>";
            }
        }
    }

    function setupAddCartAjax() {
        document.addEventListener("click", async function (event) {
            if (event.defaultPrevented) return;

            const link = event.target.closest("a[href*='/Add-Cart']");
            if (!link || link.dataset.ajaxBusy === "true") return;

            event.preventDefault();
            link.dataset.ajaxBusy = "true";
            const data = await requestJson(link.href);

            showAjaxMessage(data.message, data.success);
            if (data.success) {
                updateCartSummary(data.cart);
            }
            delete link.dataset.ajaxBusy;
        });

        document.addEventListener("submit", async function (event) {
            if (event.defaultPrevented) return;

            const form = event.target;
            const action = form && (form.getAttribute("action") || "");
            if (!form || action.indexOf("/Add-Cart") === -1) return;

            const submitter = event.submitter;
            if (submitter && submitter.name === "buyNow") return;

            event.preventDefault();
            if (submitter) submitter.disabled = true;

            const request = formRequest(form);
            const data = await requestJson(request.url, request.options);

            showAjaxMessage(data.message, data.success);
            if (data.success) {
                updateCartSummary(data.cart);
            }
            if (submitter) submitter.disabled = false;
        });
    }

    function setupCartAjax() {
        const cartPage = document.querySelector(".cart-page");
        if (!cartPage) return;

        cartPage.addEventListener("click", async function (event) {
            const qtyButton = event.target.closest(".qty-btn");
            const removeButton = event.target.closest(".item-remove");

            if (!qtyButton && !removeButton) return;

            event.preventDefault();
            const link = qtyButton || removeButton;
            const item = link.closest(".cart-item");
            const data = await requestJson(link.href);

            showAjaxMessage(data.message, data.success);
            if (!data.success && data.detail) {
                console.error("Ajax detail:", data.detail);
            }
            if (!data.success) return;

            if (qtyButton && item) {
                const qty = item.querySelector(".qty");
                const checkbox = item.querySelector(".item-checkbox");
                const total = item.querySelector(".item-total-price");

                if (qty) qty.textContent = data.quantity;
                if (checkbox) {
                    checkbox.dataset.quantity = data.quantity;
                    checkbox.dataset.total = data.itemTotal;
                }
                if (total) total.textContent = formatMoney(data.itemTotal);
            }

            if (removeButton && item) {
                item.remove();
            }

            if (data.cart) {
                updateCartSummary(data.cart);
            }
            refreshCartSelection();
        });

        const cartSelectionForm = document.getElementById("cartSelectionForm");
        if (cartSelectionForm) {
            cartSelectionForm.addEventListener("submit", async function (event) {
                const submitter = event.submitter;
                const action = submitter && submitter.getAttribute("formaction");
                const isDelete = action && action.indexOf("/DelSelectProduct") !== -1;

                if (!isDelete) return;

                event.preventDefault();
                const data = await requestJson(action, {
                    method: "POST",
                    body: new URLSearchParams(new FormData(cartSelectionForm))
                });

                showAjaxMessage(data.message, data.success);
                if (!data.success) return;

                (data.deletedIds || []).forEach(function (id) {
                    const checkbox = cartSelectionForm.querySelector(".item-checkbox[value='" + id + "']");
                    const item = checkbox && checkbox.closest(".cart-item");
                    if (item) item.remove();
                });

                updateCartSummary(data.cart);
                refreshCartSelection();
            });
        }
    }

    function setupReviewAjax() {
        document.addEventListener("submit", async function (event) {
            if (event.defaultPrevented) return;
            const form = event.target;
            const action = form.getAttribute("action") || "";
            const isReviewLike = action.indexOf("/review-like") !== -1;

            if (!isReviewLike) return;

            event.preventDefault();
            const request = formRequest(form);
            const data = await requestJson(request.url, request.options);

            showAjaxMessage(data.message, data.success);
            if (!data.success && data.detail) {
                console.error("Ajax detail:", data.detail);
            }
            if (!data.success) return;

            const count = form.querySelector("[data-helpful-count]");
            if (count) count.textContent = data.helpfulCount;
            form.classList.toggle("liked", !!data.liked);
        });
    }

    function setupFavoriteAjax() {
        document.addEventListener("submit", async function (event) {
            if (event.defaultPrevented) return;

            const form = event.target;
            const action = form && (form.getAttribute("action") || "");
            if (!form || action.indexOf("/favorite-toggle") === -1) return;

            event.preventDefault();
            const button = form.querySelector("button[type='submit']");
            if (button) button.disabled = true;

            const request = formRequest(form);
            const data = await requestJson(request.url, request.options);

            showAjaxMessage(data.message, data.success);
            if (!data.success) {
                if (button) button.disabled = false;
                return;
            }

            const favoriteButton = form.querySelector(".favorite-toggle, .image-favorite-btn");
            const icon = favoriteButton && favoriteButton.querySelector("i");
            if (favoriteButton) {
                favoriteButton.classList.toggle("active", !!data.favorite);
                favoriteButton.setAttribute("aria-pressed", data.favorite ? "true" : "false");
            }
            if (icon) {
                icon.classList.toggle("bxs-heart", !!data.favorite);
                icon.classList.toggle("bx-heart", !data.favorite);
            }

            const favoritePage = form.closest(".favourite-section");
            if (favoritePage && !data.favorite) {
                const item = form.closest(".product-item");
                if (item) item.remove();

                if (!favoritePage.querySelector(".product-item")) {
                    window.location.reload();
                    return;
                }
            }

            if (button) button.disabled = false;
        });
    }

    function setupOrderHistoryAjax() {
        document.addEventListener("submit", async function (event) {
            if (event.defaultPrevented) return;

            const form = event.target;
            const actionUrl = form && (form.getAttribute("action") || "");
            if (!form || actionUrl.indexOf("/OrderHistory") === -1) return;

            const actionInput = form.querySelector("input[name='action']");
            const action = actionInput ? actionInput.value : "";
            if (action !== "cancel" && action !== "confirmReceived") return;

            event.preventDefault();
            const submitter = event.submitter || form.querySelector("button[type='submit']");
            if (submitter) submitter.disabled = true;

            const request = formRequest(form);
            const data = await requestJson(request.url, request.options);

            showAjaxMessage(data.message, data.success);
            if (!data.success) {
                if (submitter) submitter.disabled = false;
                return;
            }

            window.setTimeout(function () {
                window.location.reload();
            }, 350);
        });
    }

    async function loadProductUrl(url, pushState) {
        const response = await fetch(url, {
            headers: {"X-Requested-With": "XMLHttpRequest"}
        });
        const html = await response.text();
        const doc = new DOMParser().parseFromString(html, "text/html");
        const nextLayout = doc.querySelector(".product-layout");
        const currentLayout = document.querySelector(".product-layout");
        const nextCount = doc.querySelector(".product-count");
        const currentCount = document.querySelector(".product-count");

        if (!nextLayout || !currentLayout) {
            window.location.href = url;
            return;
        }

        currentLayout.replaceWith(nextLayout);
        if (nextCount && currentCount) {
            currentCount.replaceWith(nextCount);
        }

        if (pushState) {
            window.history.pushState({}, "", url);
        }
    }

    function setupProductAjax() {
        const productPage = document.querySelector(".product-page");
        if (!productPage) return;

        productPage.addEventListener("change", function (event) {
            const control = event.target;
            const form = control.closest(".filter-form, .toolbar-sort");
            if (!form) return;

            if (control.matches(".category-check input")) {
                const label = control.closest(".category-check");
                if (label) label.classList.toggle("checked", control.checked);
            }

            const url = (form.getAttribute("action") || window.location.href)
                + "?" + new URLSearchParams(new FormData(form)).toString();
            loadProductUrl(url, true);
        });

        productPage.addEventListener("submit", function (event) {
            const form = event.target.closest(".filter-form, .toolbar-sort");
            if (!form) return;

            event.preventDefault();
            const url = (form.getAttribute("action") || window.location.href)
                + "?" + new URLSearchParams(new FormData(form)).toString();
            loadProductUrl(url, true);
        });

        productPage.addEventListener("click", function (event) {
            const link = event.target.closest(".pagination a, .filter-actions a");
            if (!link) return;

            event.preventDefault();
            loadProductUrl(link.href, true);
        });

        window.addEventListener("popstate", function () {
            loadProductUrl(window.location.href, false);
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        injectAjaxStyle();
        setupAddCartAjax();
        setupCartAjax();
        setupProductAjax();
        setupReviewAjax();
        setupFavoriteAjax();
        setupOrderHistoryAjax();
    });
})();
