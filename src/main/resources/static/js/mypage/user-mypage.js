document.addEventListener('DOMContentLoaded', () => {
    const mainContent  = document.getElementById('main-content');
    const uploadForm   = document.getElementById('uploadForm');
    const csvFile      = document.getElementById('csvFile');
    const uploadZone   = document.getElementById('uploadZone');
    const uploadProgress = document.getElementById('uploadProgress');
    const progressFill   = document.getElementById('progressFill');
    const progressText   = document.getElementById('progressText');

    // ── i18n 메시지 (data 속성에서 로드) ──────────────────────────
    const MSG = {
        confirmDelete : mainContent.dataset.confirmDelete,
        successUpload : mainContent.dataset.successUpload,
        errorUpload   : mainContent.dataset.errorUpload,
        errorNetwork  : mainContent.dataset.errorNetwork,
        errorFileType : mainContent.dataset.errorFileType,
        errorFileSize : mainContent.dataset.errorFileSize,
        uploadUrl     : mainContent.dataset.uploadUrl,
    };

    const MAX_SIZE_MB = 50;

    // ── 파일 유효성 검사 ──────────────────────────────────────────
    function validateFile(file) {
        if (!file.name.toLowerCase().endsWith('.csv')) {
            alert(MSG.errorFileType);
            return false;
        }
        if (file.size > MAX_SIZE_MB * 1024 * 1024) {
            alert(MSG.errorFileSize);
            return false;
        }
        return true;
    }

    // ── 업로드 실행 (XMLHttpRequest - 프로그레스 지원) ────────────
    function uploadFile(file) {
        if (!validateFile(file)) return;

        const formData = new FormData();
        formData.append('file', file);

        uploadProgress.style.display = 'block';
        progressFill.style.width = '0%';

        const xhr = new XMLHttpRequest();

        xhr.upload.addEventListener('progress', (e) => {
            if (e.lengthComputable) {
                const pct = Math.round((e.loaded / e.total) * 100);
                progressFill.style.width = pct + '%';
                progressText.textContent = `업로드 중... ${pct}%`;
            }
        });

        xhr.addEventListener('load', () => {
            uploadProgress.style.display = 'none';
            progressFill.style.width = '0%';

            if (xhr.status >= 200 && xhr.status < 300) {
                alert(MSG.successUpload);
                location.reload();
            } else {
                alert(MSG.errorUpload + ' (' + xhr.status + ')');
            }
        });

        xhr.addEventListener('error', () => {
            uploadProgress.style.display = 'none';
            alert(MSG.errorNetwork);
        });

        xhr.open('POST', MSG.uploadUrl);
        xhr.send(formData);
    }

    // ── 파일 input 변경 ───────────────────────────────────────────
    if (csvFile) {
        csvFile.addEventListener('change', () => {
            if (csvFile.files.length > 0) {
                uploadFile(csvFile.files[0]);
                csvFile.value = ''; // 같은 파일 재업로드 허용
            }
        });
    }

    // ── 드래그 앤 드롭 ────────────────────────────────────────────
    if (uploadZone) {
        ['dragenter', 'dragover'].forEach(evt => {
            uploadZone.addEventListener(evt, (e) => {
                e.preventDefault();
                uploadZone.classList.add('drag-over');
            });
        });

        ['dragleave', 'dragend'].forEach(evt => {
            uploadZone.addEventListener(evt, () => {
                uploadZone.classList.remove('drag-over');
            });
        });

        uploadZone.addEventListener('drop', (e) => {
            e.preventDefault();
            uploadZone.classList.remove('drag-over');

            const files = e.dataTransfer.files;
            if (files.length > 0) {
                uploadFile(files[0]);
            }
        });
    }

    // ── 프로필 수정 버튼 ──────────────────────────────────────────
    const editProfileBtn = document.getElementById('editProfileBtn');
    if (editProfileBtn) {
        editProfileBtn.addEventListener('click', () => {
            // 필요시 모달 또는 페이지 이동으로 교체
            window.location.href = '/api/v1/users/update/edit';
        });
    }
});

// ── 파일 삭제 (전역: th:onclick에서 호출) ────────────────────────
async function deleteFile(fileId) {
    const mainContent = document.getElementById('main-content');
    const confirmMsg  = mainContent.dataset.confirmDelete;
    const errorNetwork = mainContent.dataset.errorNetwork;

    if (!confirm(confirmMsg)) return;

    try {
        const response = await fetch(`/api/v1/user/files/${fileId}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.ok) {
            location.reload();
        } else {
            const msg = await response.text();
            alert(msg);
        }
    } catch (error) {
        console.error('Delete error:', error);
        alert(errorNetwork);
    }
}
