<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="headerKeyword" value="${not empty param.keyword ? param.keyword : keyword}" />

<style id="hhUserNotificationCriticalCss">
    .hh-header-inner{grid-template-columns:280px minmax(340px,1fr)198px!important;gap:34px!important}
    .hh-actions{display:flex!important;align-items:center!important;justify-content:flex-end!important;gap:10px!important;min-width:198px!important}
    .hh-notification-wrapper{position:relative!important;width:42px!important;height:42px!important;flex:0 0 42px!important;display:inline-flex!important;align-items:center!important;justify-content:center!important}
    .hh-notification-toggle{width:42px!important;height:42px!important;display:inline-flex!important;align-items:center!important;justify-content:center!important;border:0!important;padding:0!important;border-radius:50%!important;color:#fff!important;background:rgba(255,255,255,.18)!important;font-size:23px!important;line-height:1!important}
    .hh-notification-toggle:hover{transform:translateY(-2px);background:rgba(255,255,255,.28)!important}
    .hh-notification-toggle .bxs-bell,.hh-notification-toggle .bx-bell{display:block!important;color:#fff!important;font-size:23px!important;line-height:1!important}
    .hh-notification-count{position:absolute!important;top:-6px!important;right:-6px!important;min-width:19px!important;height:19px!important;padding:0 5px!important;border-radius:999px!important;background:#ff4d6d!important;color:#fff!important;font-size:11px!important;font-weight:900!important;line-height:19px!important;text-align:center!important;box-shadow:0 6px 12px rgba(255,77,109,.28)!important}
    .hh-notification-count[hidden]{display:none!important}
    .hh-notification-dropdown{position:absolute!important;top:52px!important;right:0!important;width:min(390px,calc(100vw - 24px))!important;max-height:500px!important;display:none!important;overflow:hidden!important;border:1px solid rgba(17,153,142,.16)!important;border-radius:22px!important;background:#fff!important;box-shadow:0 24px 70px rgba(15,23,42,.22)!important;z-index:12050!important;color:#263238!important;text-align:left!important;white-space:normal!important}
    .hh-notification-wrapper.active .hh-notification-dropdown{display:block!important}
    .hh-notification-head{display:flex!important;align-items:center!important;justify-content:space-between!important;gap:12px!important;padding:14px 15px!important;border-bottom:1px solid #edf3f2!important;background:linear-gradient(135deg,#f6fffb,#effdf6)!important}
    .hh-notification-head strong{color:#18363a!important;font-size:15px!important;font-weight:900!important;line-height:1.3!important}
    .hh-notification-read-all{display:none;border:0!important;border-radius:999px!important;padding:7px 10px!important;background:#e5fff3!important;color:#0b8b76!important;font-size:12px!important;font-weight:800!important;line-height:1.2!important}
    .hh-notification-list{max-height:392px!important;overflow-y:auto!important;padding:8px 7px!important;background:#fff!important}
    .hh-notification-empty,.hh-notification-login{padding:18px!important;color:#667784!important;text-align:center!important;line-height:1.55!important;font-size:14px!important}
    .hh-notification-login strong{display:block!important;color:#1f3338!important;margin-bottom:6px!important;font-size:15px!important}
    .hh-notification-login p{margin:0 0 12px!important;font-size:13px!important;color:#667784!important}
    .hh-notification-login a{display:inline-flex!important;align-items:center!important;justify-content:center!important;padding:9px 16px!important;border-radius:999px!important;background:linear-gradient(135deg,#11998e,#38ef7d)!important;color:#fff!important;font-weight:900!important;text-decoration:none!important}
    .hh-notification-item{position:relative!important;display:flex!important;gap:11px!important;padding:12px 13px!important;border-radius:16px!important;color:#263238!important;text-decoration:none!important;transition:background .18s ease,transform .18s ease!important;line-height:1.35!important}
    .hh-notification-item:hover{background:#effdf6!important;transform:translateY(-1px)!important}
    .hh-notification-item.is-read{opacity:.76!important}
    .hh-notification-dot{width:9px!important;height:9px!important;margin-top:8px!important;border-radius:50%!important;background:#b7c4cc!important;flex:0 0 9px!important}
    .hh-notification-item.is-unread .hh-notification-dot{background:#ff4d6d!important;box-shadow:0 0 0 4px rgba(255,77,109,.13)!important}
    .hh-notification-body{min-width:0!important;display:grid!important;gap:4px!important}
    .hh-notification-body b{color:#20343a!important;font-size:14px!important;font-weight:800!important;line-height:1.35!important}
    .hh-notification-body small{display:block!important;color:#647583!important;font-size:12.5px!important;line-height:1.5!important;font-weight:500!important}
    .hh-notification-time{display:block!important;color:#93a1aa!important;font-size:11.5px!important;font-weight:700!important;line-height:1.2!important}
    @media(max-width:1050px){.hh-header-inner{grid-template-columns:240px minmax(260px,1fr)186px!important}.hh-actions{min-width:186px!important}}
    @media(max-width:900px){.hh-header-inner{grid-template-columns:1fr auto!important}.hh-actions{min-width:0!important}.hh-notification-dropdown{right:-96px!important}}
</style>
<header class="hh-header">
    <c:if test="${not empty sessionScope.toastMessage}">
        <div class="hh-toast ${empty sessionScope.toastType ? 'hh-toast-success' : sessionScope.toastType}">
            <i class="bx ${empty sessionScope.toastIcon ? 'bx-check-circle' : sessionScope.toastIcon}"></i>
            <span>${sessionScope.toastMessage}</span>
        </div>
        <c:remove var="toastMessage" scope="session"/>
        <c:remove var="toastType" scope="session"/>
        <c:remove var="toastIcon" scope="session"/>
    </c:if>
    <c:if test="${empty sessionScope.user}">
        <div class="hh-login-modal ${sessionScope.showLoginModal ? 'show' : ''}" id="hhLoginModal" aria-hidden="${sessionScope.showLoginModal ? 'false' : 'true'}">
            <div class="hh-login-dialog" role="dialog" aria-modal="true" aria-labelledby="hhLoginTitle">
                <button type="button" class="hh-login-close" aria-label="Đóng thông báo">
                    <i class="bx bx-x"></i>
                </button>
                <div class="hh-login-icon">
                    <i class="bx bx-cart-add"></i>
                </div>
                <h3 id="hhLoginTitle">Vui lòng đăng nhập</h3>
                <p>Bạn cần đăng nhập để thực hiện thao tác này.</p>
                <a class="hh-login-button" href="${pageContext.request.contextPath}/SignIn?redirect=${sessionScope.redirectAfterLogin}">Đăng nhập</a>
            </div>
        </div>
        <c:remove var="showLoginModal" scope="session"/>
    </c:if>
    <div class="hh-header-main">
        <div class="hh-header-inner">
            <a class="hh-logo" href="${pageContext.request.contextPath}/home">
                <span>Handmade</span>
                <b>House</b>
            </a>
            <form class="hh-search-form" action="${pageContext.request.contextPath}/product" method="get" autocomplete="off">
                <input class="hh-search-input" type="text" name="keyword" value="${headerKeyword}" placeholder="Tìm: túi + len" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
                <button class="hh-search-button" type="submit" aria-label="Tìm kiếm"><i class="bx bx-search-alt-2"></i></button>
            </form>
            <div class="hh-actions">
                <a class="hh-action" href="${pageContext.request.contextPath}/favorite" aria-label="Yêu thích"><i class="bx bx-heart"></i></a>
                <div class="hh-notification-wrapper" data-logged-in="${not empty sessionScope.user}">
                    <button type="button" class="hh-action hh-notification-toggle" aria-label="Thông báo" aria-expanded="false">
                        <i class="bx bxs-bell"></i>
                        <span class="hh-notification-count" hidden>0</span>
                    </button>
                    <div class="hh-notification-dropdown" aria-live="polite">
                        <div class="hh-notification-head">
                            <strong>Thông báo</strong>
                            <button type="button" class="hh-notification-read-all">Đã đọc hết</button>
                        </div>
                        <div class="hh-notification-list">
                            <div class="hh-notification-empty">Đang tải thông báo...</div>
                        </div>
                    </div>
                </div>
                <a class="hh-action" href="${pageContext.request.contextPath}/cart" aria-label="Giỏ hàng">
                    <i class="bx bx-cart"></i>
                    <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                        <span class="hh-cart-count">${sessionScope.cart.totalQuantity}</span>
                    </c:if>
                </a>
                <a class="hh-action" href="${pageContext.request.contextPath}/Account" aria-label="Tài khoản"><i class="bx bx-user"></i></a>
            </div>
        </div>
    </div>
    <nav class="hh-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/product">Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/jsp/blog.jsp">Blog</a>
        <a href="${pageContext.request.contextPath}/contact">Liên hệ</a>
    </nav>
</header>
<script>window.APP_CONTEXT='${pageContext.request.contextPath}';</script>

<script>
    (function () {
        const contextPath = window.APP_CONTEXT || '';
        const wrapper = document.querySelector('.hh-notification-wrapper');
        if (!wrapper) return;

        const toggle = wrapper.querySelector('.hh-notification-toggle');
        const dropdown = wrapper.querySelector('.hh-notification-dropdown');
        const list = wrapper.querySelector('.hh-notification-list');
        const badge = wrapper.querySelector('.hh-notification-count');
        const readAll = wrapper.querySelector('.hh-notification-read-all');
        const loggedIn = wrapper.dataset.loggedIn === 'true';

        function escapeHtml(value) {
            return String(value == null ? '' : value)
                .replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&#39;');
        }

        function getField(item, names, fallback) {
            for (const name of names) {
                if (item && item[name] !== undefined && item[name] !== null) {
                    return item[name];
                }
            }
            return fallback;
        }

        function setBadge(count) {
            const total = Number(count) || 0;
            if (total > 0) {
                badge.hidden = false;
                badge.textContent = total > 99 ? '99+' : String(total);
            } else {
                badge.hidden = true;
                badge.textContent = '0';
            }
        }

        function renderLoggedOut() {
            setBadge(0);
            readAll.style.display = 'none';
            list.innerHTML = '<div class="hh-notification-login">'
                + '<strong>Đăng nhập để xem thông báo</strong>'
                + '<p>Các phản hồi liên hệ, đơn hàng và đánh giá sẽ nằm gọn ở đây.</p>'
                + '<a href="' + contextPath + '/SignIn">Đăng nhập</a>'
                + '</div>';
        }

        function renderError(message) {
            readAll.style.display = 'none';
            list.innerHTML = '<div class="hh-notification-empty">'
                + escapeHtml(message || 'Chưa tải được thông báo.')
                + '</div>';
        }

        function trimText(value) {
            return String(value == null ? '' : value).replace(/\s+/g, ' ').trim();
        }

        function afterColon(value) {
            const text = trimText(value);
            const index = text.indexOf(':');
            return index >= 0 ? trimText(text.substring(index + 1)) : text;
        }

        function cleanGhnMessage(message) {
            let text = trimText(message) || 'GHN đã cập nhật trạng thái giao hàng cho đơn hàng của bạn.';
            text = text.replace(/^GHN\s*:\s*/i, '');
            text = text.replace(/^GHN\s+/i, '');
            text = text.replace(/giao\s+(tới|đến|cho)\s+khách hàng/gi, 'giao đơn hàng đến bạn');
            text = text.replace(/giao\s+(tới|đến|cho)\s+khách/gi, 'giao đơn hàng đến bạn');
            text = text.replace(/cho\s+khách hàng/gi, 'cho bạn');
            text = text.replace(/cho\s+khách/gi, 'cho bạn');
            text = text.replace(/khách hàng/gi, 'bạn');
            text = text.replace(/\bkhách\b/gi, 'bạn');
            if (/đang\s+giao/i.test(text) && !/bạn/i.test(text)) {
                text = 'đang giao đơn hàng đến bạn';
            }
            if (/(giao|vận chuyển|ship)/i.test(text) && !/^GHN\b/i.test(text)) {
                text = 'GHN ' + text;
            }
            return text || 'GHN đã cập nhật trạng thái giao hàng cho đơn hàng của bạn.';
        }

        function buildOrderStatusTitle(status) {
            const lower = status.toLowerCase();
            if (lower.includes('xác nhận')) return 'Handmade House đã xác nhận đơn hàng';
            if (lower.includes('xử lý')) return 'Handmade House đang xử lý đơn hàng';
            if (lower.includes('giao')) return 'Đơn hàng đang được giao đến bạn';
            if (lower.includes('hoàn thành')) return 'Đơn hàng đã hoàn thành';
            if (lower.includes('hủy') || lower.includes('huỷ')) return 'Đơn hàng đã bị hủy';
            return 'Handmade House đã cập nhật đơn hàng';
        }

        function normalizeNotification(item) {
            const rawTitle = trimText(getField(item, ['title', 'Title'], 'Thông báo mới'));
            const rawMessage = trimText(getField(item, ['message', 'Message'], ''));
            const type = trimText(getField(item, ['type', 'Type'], 'INFO')).toUpperCase();
            const sourceId = getField(item, ['sourceId', 'sourceid', 'Source_Id', 'source_id'], '');
            const orderText = sourceId ? 'Đơn hàng #' + sourceId + '. ' : '';
            let title = rawTitle;
            let message = rawMessage;

            if (type === 'ORDER_STATUS') {
                const status = afterColon(rawMessage || rawTitle);
                title = buildOrderStatusTitle(status);
                message = orderText + (status ? 'Handmade House đã chuyển trạng thái đơn hàng sang: ' + status + '. ' : '') + 'Nhấn để xem chi tiết đơn hàng.';
            } else if (type === 'ORDER_CREATED') {
                title = 'Handmade House đã nhận đơn hàng của bạn';
                message = (rawMessage || orderText || 'Đơn hàng mới đã được tạo.') + ' Handmade House sẽ sớm xác nhận và chuẩn bị đơn hàng cho bạn.';
            } else if (type === 'ORDER_SHIPPING') {
                const shipping = cleanGhnMessage(rawMessage);
                title = shipping.toLowerCase().includes('đang giao') ? 'GHN đang giao đơn hàng đến bạn' : 'GHN đã cập nhật giao hàng';
                message = orderText + shipping + '. Nhấn để xem chi tiết giao hàng.';
            } else if (type === 'PAYMENT_SUCCESS' || type === 'PAYMENT') {
                title = 'Handmade House đã cập nhật thanh toán của bạn';
                message = rawMessage || (orderText + 'Nhấn để xem chi tiết thanh toán.');
            } else if (type === 'CONTACT_REPLY') {
                title = 'Handmade House đã phản hồi liên hệ của bạn';
                message = rawMessage || 'Nhấn để xem nội dung phản hồi.';
            } else if (type === 'REVIEW_REPLY' || type === 'REVIEW_APPROVED' || type === 'REVIEW_RESPONSE_APPROVED') {
                title = 'Handmade House đã phản hồi đánh giá của bạn';
                message = rawMessage || 'Nhấn để nhảy tới bình luận được phản hồi.';
            }

            return { title: title, message: message };
        }

        function renderNotifications(items) {
            items = Array.isArray(items) ? items : [];
            readAll.style.display = items.length ? 'inline-flex' : 'none';
            if (!items.length) {
                list.innerHTML = '<div class="hh-notification-empty">Chưa có thông báo nào.</div>';
                return;
            }

            list.innerHTML = items.map(function (item) {
                const id = getField(item, ['notificationId', 'notificationid', 'Notification_Id', 'notification_id'], 0);
                const createdAt = getField(item, ['createdAt', 'createdat', 'Create_At', 'create_at'], '');
                const rawRead = getField(item, ['isRead', 'isread', 'Is_Read', 'is_read'], 0);
                const isRead = rawRead === true || rawRead === 1 || rawRead === '1' || rawRead === 'true';
                const href = contextPath + '/notifications/go?id=' + encodeURIComponent(id);
                const view = normalizeNotification(item);
                return '<a class="hh-notification-item ' + (isRead ? 'is-read' : 'is-unread') + '" href="' + href + '">'
                    + '<span class="hh-notification-dot"></span>'
                    + '<span class="hh-notification-body">'
                    + '<b>' + escapeHtml(view.title) + '</b>'
                    + (view.message ? '<small>' + escapeHtml(view.message) + '</small>' : '')
                    + (createdAt ? '<time class="hh-notification-time">' + escapeHtml(createdAt) + '</time>' : '')
                    + '</span>'
                    + '</a>';
            }).join('');
        }

        function parseJsonResponse(res) {
            return res.text().then(function (text) {
                let data = null;
                try {
                    data = text ? JSON.parse(text) : null;
                } catch (error) {
                    throw new Error('Endpoint /notifications không trả JSON. HTTP ' + res.status);
                }
                if (!res.ok) {
                    throw new Error((data && data.message) ? data.message : ('HTTP ' + res.status));
                }
                return data;
            });
        }

        function loadNotifications() {
            if (!loggedIn) {
                renderLoggedOut();
                return;
            }
            fetch(contextPath + '/notifications?ajax=1&_=' + Date.now(), {
                headers: {
                    'Accept': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'include',
                cache: 'no-store'
            })
                .then(parseJsonResponse)
                .then(function (data) {
                    if (!data || !data.loggedIn) {
                        renderLoggedOut();
                        return;
                    }
                    if (data.success === false) {
                        setBadge(data.unreadCount || 0);
                        renderError(data.message || 'Không đọc được thông báo từ máy chủ.');
                        return;
                    }
                    setBadge(data.unreadCount || 0);
                    renderNotifications(data.notifications || []);
                })
                .catch(function (error) {
                    renderError(error && error.message ? error.message : 'Chưa tải được thông báo.');
                });
        }

        toggle.addEventListener('click', function (event) {
            event.stopPropagation();
            const active = wrapper.classList.toggle('active');
            toggle.setAttribute('aria-expanded', active ? 'true' : 'false');
            if (active) loadNotifications();
        });

        document.addEventListener('click', function (event) {
            if (!wrapper.contains(event.target)) {
                wrapper.classList.remove('active');
                toggle.setAttribute('aria-expanded', 'false');
            }
        });

        readAll.addEventListener('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            if (!loggedIn) return;
            fetch(contextPath + '/notifications/read', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                    'Accept': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                credentials: 'include',
                cache: 'no-store',
                body: 'action=markAllRead'
            })
                .then(parseJsonResponse)
                .then(function (data) {
                    setBadge(data.unreadCount || 0);
                    loadNotifications();
                })
                .catch(function (error) {
                    renderError(error && error.message ? error.message : 'Chưa cập nhật được thông báo.');
                });
        });

        loadNotifications();
    })();
</script>

<c:if test="${empty sessionScope.user}">
    <script>
        (function () {
            const modal = document.getElementById('hhLoginModal');
            if (!modal) return;

            const loginLink = modal.querySelector('.hh-login-button');
            const closeButtons = modal.querySelectorAll('.hh-login-close');
            const contextPath = window.APP_CONTEXT || '';

            function currentRedirect() {
                let current = window.location.pathname + window.location.search;
                if (contextPath && current.startsWith(contextPath)) {
                    current = current.substring(contextPath.length);
                }
                current = current.replace(/^\/+/, '');
                return current || 'home';
            }

            function syncLoginLink() {
                loginLink.href = contextPath + '/SignIn?redirect=' + encodeURIComponent(currentRedirect());
            }

            function openLoginModal(event) {
                event.preventDefault();
                syncLoginLink();
                modal.classList.add('show');
                modal.setAttribute('aria-hidden', 'false');
            }

            function closeLoginModal() {
                modal.classList.remove('show');
                modal.setAttribute('aria-hidden', 'true');
            }

            if (modal.classList.contains('show')) {
                syncLoginLink();
            }

            document.querySelectorAll('a[href*="/Add-Cart"]').forEach(function (link) {
                link.addEventListener('click', openLoginModal);
            });

            document.querySelectorAll('form[action$="/Add-Cart"]').forEach(function (form) {
                form.addEventListener('submit', openLoginModal);
            });

            document.querySelectorAll('form[action$="/favorite-toggle"]').forEach(function (form) {
                form.addEventListener('submit', openLoginModal);
            });

            document.addEventListener('click', function (event) {
                const addCartLink = event.target.closest('a[href*="/Add-Cart"]');
                if (addCartLink) {
                    openLoginModal(event);
                    event.stopImmediatePropagation();
                }
            }, true);

            document.addEventListener('submit', function (event) {
                const form = event.target;
                if (!form || !form.matches('form[action$="/Add-Cart"], form[action$="/favorite-toggle"]')) {
                    return;
                }
                openLoginModal(event);
                event.stopImmediatePropagation();
            }, true);

            closeButtons.forEach(function (button) {
                button.addEventListener('click', closeLoginModal);
            });

            modal.addEventListener('click', function (event) {
                if (event.target === modal) {
                    closeLoginModal();
                }
            });

            document.addEventListener('keydown', function (event) {
                if (event.key === 'Escape') {
                    closeLoginModal();
                }
            });
        })();
    </script>
</c:if>
