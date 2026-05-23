(function () {
    const debounce = (fn, delay) => {
        let timer;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => fn.apply(this, args), delay);
        };
    };

    const getContextPath = (form) => {
        if (window.APP_CONTEXT !== undefined) return window.APP_CONTEXT;
        try {
            const action = new URL(form.action, window.location.origin).pathname;
            return action.replace(/\/product\/?$/, "");
        } catch (e) {
            return "";
        }
    };

    const formatPrice = (price) => {
        const number = Number(price || 0);
        return new Intl.NumberFormat("vi-VN").format(number) + " đ";
    };

    const escapeHtml = (value) => String(value || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

    const buildSuggestion = (contextPath, product) => {
        const image = product.imageUrl && product.imageUrl.trim() !== ""
            ? product.imageUrl
            : contextPath + "/images/no-image.png";
        const sold = product.sold && product.sold > 0
            ? `<span class="suggestion-sold"><i class='bx bx-trending-up'></i> Đã bán ${product.sold}</span>`
            : `<span class="suggestion-sold muted">Sản phẩm phù hợp</span>`;

        return `
            <a class="search-suggestion-item" href="${contextPath}/product-detail?id=${product.productId}">
                <img src="${escapeHtml(image)}" alt="${escapeHtml(product.productName)}">
                <span class="suggestion-info">
                    <strong>${escapeHtml(product.productName)}</strong>
                    <small>${escapeHtml(product.categoryName || "Handmade")}</small>
                    <span class="suggestion-bottom">
                        <b>${formatPrice(product.productPrice)}</b>
                        ${sold}
                    </span>
                </span>
            </a>
        `;
    };

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".search-form").forEach(function (form) {
            const input = form.querySelector(".search-input[name='keyword']");
            if (!input) return;

            form.classList.add("search-suggest-ready");

            let box = form.querySelector(".search-suggestion-box");
            if (!box) {
                box = document.createElement("div");
                box.className = "search-suggestion-box";
                form.appendChild(box);
            }

            const contextPath = getContextPath(form);

            const closeBox = () => {
                box.classList.remove("show");
                box.innerHTML = "";
            };

            const renderEmpty = (message) => {
                box.innerHTML = `<div class="search-suggestion-empty">${message}</div>`;
                box.classList.add("show");
            };

            const fetchSuggestions = debounce(function () {
                const keyword = input.value.trim();

                if (keyword.length < 2) {
                    closeBox();
                    return;
                }

                fetch(`${contextPath}/search-suggest?keyword=${encodeURIComponent(keyword)}`, {
                    headers: {"Accept": "application/json"}
                })
                    .then(response => response.ok ? response.json() : [])
                    .then(products => {
                        if (!Array.isArray(products) || products.length === 0) {
                            renderEmpty("Chưa tìm thấy sản phẩm phù hợp");
                            return;
                        }

                        box.innerHTML = `
                            <div class="search-suggestion-title">Gợi ý bán chạy liên quan</div>
                            ${products.map(product => buildSuggestion(contextPath, product)).join("")}
                        `;
                        box.classList.add("show");
                    })
                    .catch(() => closeBox());
            }, 220);

            input.addEventListener("input", fetchSuggestions);
            input.addEventListener("focus", fetchSuggestions);

            document.addEventListener("click", function (event) {
                if (!form.contains(event.target)) {
                    closeBox();
                }
            });
        });
    });
})();
