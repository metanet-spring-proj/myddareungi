
  const notifications = [
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
  ];

  const notificationTrigger = document.getElementById("notificationTrigger");
  const notificationPopover = document.getElementById("notificationPopover");
  const notificationList = document.getElementById("notificationList");
  const notificationCount = document.getElementById("notificationCount");
  const notificationDot = document.getElementById("notificationDot");
  const markAllReadBtn = document.getElementById("markAllReadBtn");
  const viewAllBtn = document.getElementById("viewAllBtn");

  function getUnreadCount() {
    return notifications.filter(item => !item.isRead).length;
  }

  function updateUnreadUI() {
    const unreadCount = getUnreadCount();
    notificationCount.textContent = unreadCount;

    if (unreadCount > 0) {
      notificationDot.classList.add("show");
    } else {
      notificationDot.classList.remove("show");
    }
  }

  function renderNotifications() {
    if (notifications.length === 0) {
      notificationList.innerHTML = `
        <div class="notification-empty">표시할 알림이 없습니다.</div>
      `;
      updateUnreadUI();
      return;
    }

    notificationList.innerHTML = notifications.map(item => `
      <div class="notification-item ${item.isRead ? "" : "unread"}" data-id="${item.id}">
        <div class="notification-item-top">
          <div class="notification-item-title">${item.title}</div>
          <div class="notification-item-time">${item.timeText}</div>
        </div>
        <div class="notification-item-message">${item.message}</div>
      </div>
    `).join("");

    updateUnreadUI();
  }

  function openPopover() {
    notificationPopover.hidden = false;
    notificationTrigger.setAttribute("aria-expanded", "true");
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
    const target = notifications.find(n => n.id === id);

    if (target) {
      target.isRead = true;
      renderNotifications();

      console.log("클릭한 알림:", target);
      // 여기서 상세 페이지 이동 가능
      // location.href = "/notification/" + id;
    }
  });

  markAllReadBtn.addEventListener("click", function () {
    notifications.forEach(item => {
      item.isRead = true;
    });
    renderNotifications();
  });

  viewAllBtn.addEventListener("click", function () {
    console.log("전체 보기 클릭");
    // location.href = "/notifications";
  });

  renderNotifications();
