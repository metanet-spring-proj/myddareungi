
 /* const notifications = [
    {
      id: 1,
      title: "업로드 검토 완료",
      message: "요청하신 2024년 4분기 스테이션 이용률 데이터 검토가 완료되었습니다.",
      timeText: "09:15 AM",
      isRead: false
    },
    {
      id: 2,
      title: "시스템 점검 안내",
      message: "서버 안정화 작업을 위해 금일 00시부터 시스템 점검이 예정되어 있습니다.",
      timeText: "어제",
      isRead: false
    },
    {
      id: 3,
      title: "새로운 데이터 업데이트",
      message: "신규 탄소절감량 데이터 세트가 반영되었습니다. 대시보드에서 확인하세요.",
      timeText: "2일 전",
      isRead: false
    }
  ];*/

  const notificationTrigger = document.getElementById("notificationTrigger");
  const notificationPopover = document.getElementById("notificationPopover");
  const notificationList = document.getElementById("notificationList");
  const notificationCount = document.getElementById("notificationCount");
  const notificationDot = document.getElementById("notificationDot");
  const markAllReadBtn = document.getElementById("markAllReadBtn");
  const viewAllBtn = document.getElementById("viewAllBtn");
  const bellIcon = document.getElementById("bell-icon");
  
  function getCookie(name) {
          let value = "; " + document.cookie;
          let parts = value.split("; " + name + "=");
          if (parts.length === 2) return parts.pop().split(";").shift();
          return null;
	}

	function getCsrfToken() {
	      return document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || getCookie('XSRF-TOKEN');
	  }
	  function getCsrfHeader() {
	      return document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-XSRF-TOKEN';
	  }
	  
	  
  function formatTime(timestamp) {
        const now = new Date();
        const created = new Date(timestamp);

        const nowDate = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const createdDate = new Date(created.getFullYear(), created.getMonth(), created.getDate());
        const diffDays = Math.round((nowDate - createdDate) / (1000 * 60 * 60 * 24));

        if (diffDays === 0) {
            return created.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
        } else if (diffDays === 1) {
            return '어제';
        } else {
            return `${diffDays}일 전`;
        }
    }
	  
	  
  
 /* function getUnreadCount() {
    return notifications.filter(item => !item.isRead).length;
  }*/

  function updateUnreadUI(unreadCount) {
        notificationCount.textContent = unreadCount;
        if (unreadCount > 0) {
            notificationDot.classList.add("show");
        } else {
            notificationDot.classList.remove("show");
        }
    }

	function renderNotifications(notifications) {
	    if (notifications.length === 0) {
	        notificationList.innerHTML = `
	            <div class="notification-empty">표시할 알림이 없습니다.</div>
	        `;
	        updateUnreadUI(0);
	        return;
	    }
	
	    notificationList.innerHTML = notifications.map(item => `
	        <div class="notification-item ${item.isRead === 0 ? "unread" : ""}" data-id="${item.notificationId}">
	            <div class="notification-item-top">
	                <div class="notification-item-title">${item.notificationType}</div>
	                <div class="notification-item-time">${formatTime(item.createdAt)}</div>
	            </div>
	            <div class="notification-item-message">${item.message}</div>
	        </div>
	    `).join("");
	
	    const unreadCount = notifications.filter(n => n.isRead === 0).length;
	    updateUnreadUI(unreadCount);
	}

	function loadNotifications() {
	      fetch('/api/v1/notifications/unread')
		  	.then(res => res.json())
	      	.then(data => renderNotifications(data));
	  }
	
	
	function openPopover() {
	  notificationPopover.hidden = false;
	  notificationTrigger.setAttribute("aria-expanded", "true");
	  loadNotifications();
	}

	function closePopover() {
	    notificationPopover.hidden = true;
	    notificationTrigger.setAttribute("aria-expanded", "false");
	}

  function togglePopover() {
    const isHidden = notificationPopover.hidden;
    if (isHidden) {
      openPopover();
    } else {
      closePopover();
    }
  }

  notificationTrigger.addEventListener("click", function (e) {
    e.stopPropagation();
    togglePopover();
  });

  notificationPopover.addEventListener("click", function (e) {
    e.stopPropagation();
  });

  document.addEventListener("click", function () {
    closePopover();
  });

  document.addEventListener("keydown", function (e) {
    if (e.key === "Escape") {
      closePopover();
    }
  });

	notificationList.addEventListener("click", function (e) {
		const item = e.target.closest(".notification-item");
	      if (!item) return;

	      const id = Number(item.dataset.id);
	      fetch(`/api/v1/notifications/${id}/read`, {
	          method: 'PATCH',
	          headers: { [getCsrfHeader()]: getCsrfToken() }
	      }).then(() => loadNotifications());
	  });

	markAllReadBtn.addEventListener("click", function () {
        fetch('/api/v1/notifications/read', {
            method: 'PATCH',
            headers: { [getCsrfHeader()]: getCsrfToken()}
        }).then(() => loadNotifications());
    });

	viewAllBtn.addEventListener("click", function () {
		fetch('/api/v1/notifications')
			.then(res => res.json())
		    .then(data => renderNotifications(data));
	  });
	  
  // 페이지 로드 시 읽지 않은 알림 dot 표시
	fetch('/api/v1/notifications/unread')
	    .then(res => res.json())
	    .then(data => updateUnreadUI(data.length));
		
	// SSE 연결                                                                                                                        
	const eventSource = new EventSource('/api/v1/notifications/subscribe');
	eventSource.addEventListener('notification', function(e) {
    	const unreadCount = parseInt(notificationCount.textContent) || 0;
    	notificationCount.textContent = unreadCount + 1;
    	notificationDot.classList.add("show");

      // 벨 shake
    	bellIcon.classList.add("fa-shake");
    	setTimeout(() => bellIcon.classList.remove("fa-shake"), 3000);
	});

  	eventSource.addEventListener('error', function(e) {
    	if (eventSource.readyState === EventSource.CLOSED) {
      		console.log('SSE 연결 종료');
      	}
  	});
