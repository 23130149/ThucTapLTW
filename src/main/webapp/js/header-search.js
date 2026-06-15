(function () {
    if (window.__handmadeHeaderSearchReady) return;
    window.__handmadeHeaderSearchReady = true;

    const contextPath = window.APP_CONTEXT || document.body.getAttribute('data-context-path') || '';

    function injectStyle() {
        if (document.getElementById('headerSearchRuntimeStyle')) return;
        const style = document.createElement('style');
        style.id = 'headerSearchRuntimeStyle';
        style.textContent = `
            .search-form{position:relative;overflow:visible!important;}
            .hh-suggest-box{position:absolute;left:0;right:0;top:calc(100% + 10px);display:none;background:#fff;border:1px solid #e7ecef;border-radius:18px;box-shadow:0 20px 45px rgba(9,64,58,.20);z-index:5000;overflow:hidden;min-width:260px;}
            .hh-suggest-box.show{display:block;}
            .hh-suggest-title{padding:9px 14px;font-size:12px;font-weight:800;text-transform:uppercase;letter-spacing:0;color:#11998e;background:#f6fffb;border-bottom:1px solid #eef5f2;}
            .hh-suggest-item{display:grid;grid-template-columns:52px minmax(0,1fr);gap:12px;align-items:center;padding:11px 14px;color:#17211f;text-decoration:none;border-bottom:1px solid #f2f4f3;}
            .hh-suggest-item:last-child{border-bottom:none;}
            .hh-suggest-item:hover{background:#f7fff9;}
            .hh-suggest-item img{width:52px;height:52px;border-radius:14px;object-fit:cover;background:#edf1f0;}
            .hh-suggest-info{min-width:0;display:grid;gap:2px;}
            .hh-suggest-info strong{font-size:14px;line-height:1.25;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;color:#16332f;}
            .hh-suggest-info small{font-size:12px;color:#7a8582;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;}
            .hh-suggest-info b{font-size:13px;color:#e53935;}
            .hh-suggest-empty{padding:14px;color:#7a8582;font-size:13px;text-align:center;}
        `;
        document.head.appendChild(style);
    }

    function money(value) {
        const number = Number(value || 0);
        return new Intl.NumberFormat('vi-VN').format(number) + ' đ';
    }

    function text(value) {
        return value == null ? '' : String(value);
    }

    function normalizeForm(form) {
        const input = form.querySelector('.search-input') || form.querySelector('input[type="text"], input:not([type])');
        if (!input) return null;

        if (!form.getAttribute('action') || form.getAttribute('action') === '#') {
            form.setAttribute('action', contextPath + '/product');
        }
        form.setAttribute('method', 'GET');
        form.setAttribute('autocomplete', 'off');

        input.classList.add('search-input');
        input.setAttribute('name', 'keyword');
        input.setAttribute('autocomplete', 'off');
        input.setAttribute('aria-label', 'Tìm kiếm sản phẩm');
        if (!input.getAttribute('placeholder')) {
            input.setAttribute('placeholder', 'Tìm kiếm... VD: áo + len');
        }

        let box = form.querySelector('.hh-suggest-box');
        if (!box) {
            box = document.createElement('div');
            box.className = 'hh-suggest-box';
            form.appendChild(box);
        }
        return {input, box};
    }

    function render(box, items) {
        box.innerHTML = '';
        if (!items || items.length === 0) {
            const empty = document.createElement('div');
            empty.className = 'hh-suggest-empty';
            empty.textContent = 'Chưa tìm thấy sản phẩm phù hợp';
            box.appendChild(empty);
            box.classList.add('show');
            return;
        }

        const title = document.createElement('div');
        title.className = 'hh-suggest-title';
        title.textContent = 'Gợi ý bán chạy liên quan';
        box.appendChild(title);

        items.slice(0, 4).forEach(function (p) {
            const item = document.createElement('a');
            item.className = 'hh-suggest-item';
            item.href = contextPath + '/product-detail?id=' + encodeURIComponent(p.productId || '');

            const img = document.createElement('img');
            img.src = text(p.imageUrl);
            img.alt = text(p.productName);
            img.onerror = function () { this.style.visibility = 'hidden'; };

            const info = document.createElement('span');
            info.className = 'hh-suggest-info';

            const name = document.createElement('strong');
            name.textContent = text(p.productName);

            const meta = document.createElement('small');
            meta.textContent = text(p.categoryName) + ' • Đã bán ' + Number(p.sold || 0);

            const price = document.createElement('b');
            price.textContent = money(p.productPrice);

            info.appendChild(name);
            info.appendChild(meta);
            info.appendChild(price);
            item.appendChild(img);
            item.appendChild(info);
            box.appendChild(item);
        });
        box.classList.add('show');
    }

    function setupForm(form) {
        const parts = normalizeForm(form);
        if (!parts) return;
        const input = parts.input;
        const box = parts.box;
        let timer = null;
        let controller = null;

        function hide() {
            box.classList.remove('show');
        }

        function suggest() {
            const kw = input.value.trim();
            if (kw.length < 2) {
                hide();
                return;
            }

            if (controller) controller.abort();
            controller = new AbortController();
            fetch(contextPath + '/search-suggest?keyword=' + encodeURIComponent(kw), {
                headers: {Accept: 'application/json'},
                signal: controller.signal
            })
                .then(response => response.ok ? response.json() : [])
                .then(items => render(box, items))
                .catch(() => {});
        }

        input.addEventListener('input', function () {
            clearTimeout(timer);
            timer = setTimeout(suggest, 220);
        });
        input.addEventListener('focus', function () {
            if (input.value.trim().length >= 2) suggest();
        });
        document.addEventListener('click', function (event) {
            if (!form.contains(event.target)) hide();
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        injectStyle();
        document.querySelectorAll('.search-form').forEach(setupForm);
    });
})();
