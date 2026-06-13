(function () {
    if (window.__handmadeSearchSuggestReady) return;
    window.__handmadeSearchSuggestReady = true;

    function debounce(fn, delay) {
        let timer;
        return function (...args) {
            clearTimeout(timer);
            timer = setTimeout(() => fn.apply(this, args), delay);
        };
    }

    function contextPathFrom(form) {
        if (window.APP_CONTEXT !== undefined) return window.APP_CONTEXT;
        try {
            const action = new URL(form.action, window.location.origin).pathname;
            return action.replace(/\/product\/?$/, "");
        } catch (e) {
            return "";
        }
    }

    function money(value) {
        return new Intl.NumberFormat("vi-VN").format(Number(value || 0)) + " đ";
    }

    function text(value) {
        return value == null ? "" : String(value);
    }

    function injectSearchStyle() {
        if (document.getElementById("hh-search-suggest-style")) return;
        const style = document.createElement("style");
        style.id = "hh-search-suggest-style";
        style.textContent = `
            .hh-search-form .hh-suggest-box{
                top:calc(100% + 6px)!important;
                left:0!important;
                right:0!important;
                padding:10px!important;
                border-radius:18px!important;
            }
            .hh-search-form .hh-suggest-title{
                padding:4px 0 10px!important;
                background:transparent!important;
                border-bottom:0!important;
                color:#1f2933!important;
                font-size:15px!important;
                text-transform:none!important;
                letter-spacing:0!important;
            }
            .hh-search-form .hh-suggest-item{
                grid-template-columns:74px minmax(0,1fr)!important;
                padding:8px 10px!important;
                gap:14px!important;
            }
            .hh-search-form .hh-suggest-item img{
                width:74px!important;
                height:74px!important;
                border-radius:14px!important;
            }
            .hh-search-form .hh-suggest-bottom{
                justify-content:flex-start!important;
                gap:14px!important;
            }
        `;
        document.head.appendChild(style);
    }

    function normalizeForm(form) {
        const input = form.querySelector(".hh-search-input, .search-input, input[name='keyword']");
        if (!input) return null;

        const contextPath = contextPathFrom(form);
        form.classList.add("search-suggest-ready");
        form.setAttribute("method", "GET");
        form.setAttribute("autocomplete", "off");
        if (!form.getAttribute("action") || form.getAttribute("action") === "#") {
            form.setAttribute("action", contextPath + "/product");
        }

        input.setAttribute("name", "keyword");
        input.setAttribute("autocomplete", "off");
        if (!input.getAttribute("placeholder") || input.getAttribute("placeholder") === "Tìm sản phẩm") {
            input.setAttribute("placeholder", "Tìm: túi + len");
        }

        let box = form.querySelector(".hh-suggest-box, .search-suggestion-box");
        if (!box) {
            box = document.createElement("div");
            box.className = form.classList.contains("hh-search-form") ? "hh-suggest-box" : "search-suggestion-box";
            form.appendChild(box);
        }

        return {input, box, contextPath};
    }

    function render(box, products, contextPath, keyword) {
        box.innerHTML = "";
        const hasKeyword = keyword && keyword.trim().length > 0;

        if (!Array.isArray(products) || products.length === 0) {
            const empty = document.createElement("div");
            empty.className = box.classList.contains("hh-suggest-box") ? "hh-suggest-empty" : "search-suggestion-empty";
            empty.textContent = "Chưa tìm thấy sản phẩm phù hợp";
            box.appendChild(empty);
            box.classList.add("show");
            return;
        }

        const title = document.createElement("div");
        title.className = box.classList.contains("hh-suggest-box") ? "hh-suggest-title" : "search-suggestion-title";
        title.textContent = hasKeyword ? "Kết quả phù hợp" : "Sản phẩm nổi bật";
        box.appendChild(title);

        products.slice(0, 4).forEach(function (p) {
            const item = document.createElement("a");
            item.className = box.classList.contains("hh-suggest-box") ? "hh-suggest-item" : "search-suggestion-item";
            item.href = contextPath + "/product-detail?id=" + encodeURIComponent(p.productId || "");

            const img = document.createElement("img");
            img.src = text(p.imageUrl) || contextPath + "/images/no-image.png";
            img.alt = text(p.productName);
            img.onerror = function () {
                this.src = contextPath + "/images/no-image.png";
            };

            const info = document.createElement("span");
            info.className = box.classList.contains("hh-suggest-box") ? "hh-suggest-info" : "suggestion-info";

            const name = document.createElement("strong");
            name.textContent = text(p.productName);

            const meta = document.createElement("small");
            meta.textContent = text(p.categoryName || "Handmade");

            const bottom = document.createElement("span");
            bottom.className = box.classList.contains("hh-suggest-box") ? "hh-suggest-bottom" : "suggestion-bottom";

            const price = document.createElement("b");
            price.textContent = money(p.productPrice);

            const sold = document.createElement("span");
            sold.textContent = hasKeyword
                ? "Phù hợp"
                : (Number(p.sold || 0) > 0 ? "Đã bán " + Number(p.sold || 0) : "Nổi bật");

            bottom.appendChild(price);
            bottom.appendChild(sold);
            info.appendChild(name);
            info.appendChild(meta);
            info.appendChild(bottom);
            item.appendChild(img);
            item.appendChild(info);
            box.appendChild(item);
        });

        box.classList.add("show");
    }

    function setup(form) {
        const parts = normalizeForm(form);
        if (!parts) return;

        const {input, box, contextPath} = parts;
        let controller = null;

        function close() {
            box.classList.remove("show");
            box.innerHTML = "";
        }

        const suggest = debounce(function (showDefault) {
            const keyword = input.value.trim();
            if (keyword.length < 2 && !showDefault) {
                close();
                return;
            }

            if (controller) controller.abort();
            controller = new AbortController();

            const requestKeyword = keyword;
            fetch(contextPath + "/search-suggest?keyword=" + encodeURIComponent(requestKeyword), {
                headers: {"Accept": "application/json"},
                signal: controller.signal
            })
                .then(response => response.ok ? response.json() : [])
                .then(products => {
                    const currentKeyword = input.value.trim();
                    if (currentKeyword !== requestKeyword) {
                        return;
                    }
                    render(box, products, contextPath, requestKeyword);
                })
                .catch(error => {
                    if (error.name !== "AbortError") close();
                });
        }, 200);

        input.addEventListener("input", function () {
            suggest(false);
        });
        input.addEventListener("focus", function () {
            suggest(true);
        });
        document.addEventListener("click", function (event) {
            if (!form.contains(event.target)) close();
        });
    }

    document.addEventListener("DOMContentLoaded", function () {
        injectSearchStyle();
        document.querySelectorAll(".hh-search-form, .search-form").forEach(setup);
    });
})();
