(function () {
    const root = document.getElementById('hhNotification');
    if (!root) return;

    const contextPath = window.APP_CONTEXT || '';
    const button = root.querySelector('.hh-bell-btn');
    const countBadge = root.querySelector('.hh-bell-count');
    const panel = root.querySelector('.hh-notification-panel');
    const list = root.querySelector('.hh-notification-list');
    const markAll = root.querySelector('.hh-mark-all');

    function escapeHtml(value) {
        return String(value || '')
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }

    function updateBadge(count) {
        const safeCount = Number(count || 0);
        countBadge.textContent = safeCount > 99 ? '99+' : safeCount;
        countBadge.hidden = safeCount <= 0;
    }

    function render(items) {
        if (!items || items.length === 0) {
            list.innerHTML = '<p class="hh-notification-empty">Chưa có thông báo mới.</p>';
            return;
        }

        list.innerHTML = items.map(function (item) {
            const readClass = item.read ? 'read' : 'unread';
            const icon = item.type === 'CONTACT_REPLY' ? 'bx-envelope-open' : 'bx-message-rounded-check';
            return '<a class="hh-notification-item ' + readClass + '" href="' + contextPath + escapeHtml(item.targetUrl) + '" data-id="' + item.notificationId + '">' +
                '<i class="bx ' + icon + '"></i>' +
                '<span><strong>' + escapeHtml(item.title) + '</strong>' +
                '<small>' + escapeHtml(item.message) + '</small>' +
                '<em>' + escapeHtml(item.createAt) + '</em></span>' +
                '</a>';
        }).join('');
    }

    function loadNotifications() {
        fetch(contextPath + '/notifications', {
            headers: {'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest'}
        })
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (!data || data.success === false) return;
                updateBadge(data.unreadCount);
                render(data.notifications || []);
            })
            .catch(function () {
                list.innerHTML = '<p class="hh-notification-empty">Không tải được thông báo.</p>';
            });
    }

    function markRead(notificationId) {
        if (!notificationId) return Promise.resolve();
        const body = new URLSearchParams();
        body.set('notificationId', notificationId);
        return fetch(contextPath + '/notifications', {
            method: 'POST',
            headers: {'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest'},
            body: body
        }).catch(function () {});
    }

    button.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        const opened = root.classList.toggle('open');
        button.setAttribute('aria-expanded', opened ? 'true' : 'false');
        if (opened) loadNotifications();
    });

    list.addEventListener('click', function (event) {
        const item = event.target.closest('.hh-notification-item');
        if (!item) return;
        event.preventDefault();
        markRead(item.dataset.id).finally(function () {
            window.location.href = item.href;
        });
    });

    if (markAll) markAll.addEventListener('click', function (event) {
        event.preventDefault();
        event.stopPropagation();
        const body = new URLSearchParams();
        body.set('action', 'markAllRead');
        fetch(contextPath + '/notifications', {
            method: 'POST',
            headers: {'Accept': 'application/json', 'X-Requested-With': 'XMLHttpRequest'},
            body: body
        }).then(function (res) { return res.json(); })
            .then(function (data) {
                updateBadge(data.unreadCount || 0);
                root.querySelectorAll('.hh-notification-item').forEach(function (item) {
                    item.classList.remove('unread');
                    item.classList.add('read');
                });
            })
            .catch(function () {});
    });

    document.addEventListener('click', function () {
        root.classList.remove('open');
        button.setAttribute('aria-expanded', 'false');
    });

    panel.addEventListener('click', function (event) {
        event.stopPropagation();
    });

    loadNotifications();
})();
