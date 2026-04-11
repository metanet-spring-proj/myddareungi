document.addEventListener('DOMContentLoaded', function() {
    const initVirtualSelect = () => {
        const districtSelect = document.querySelector('#districtSelect');
        if (districtSelect && typeof VirtualSelect !== 'undefined') {
            const selectedValues = Array.from(districtSelect.options)
                                        .filter(opt => opt.selected || opt.hasAttribute('selected'))
                                        .map(opt => opt.value);

            VirtualSelect.init({
                ele: '#districtSelect',
                placeholder: window.I18N?.districtPlaceholder || '자치구 선택',
                noOptionsText: window.I18N?.noOptionsText || '데이터가 없습니다',
                selectAllText: window.I18N?.selectAllText || '전체 선택',
                searchPlaceholder: window.I18N?.searchPlaceholder || '검색...',
                multiple: true,
                search: true,
                showSelectedOptionsFirst: true,
                maxWidth: '100%',
                position: 'auto',
                dropboxWidth: 'auto',
                hideClearButton: false,
                showValueAsTags: true,
                selectedValue: selectedValues,
            });
        }
    }; 
    initVirtualSelect();
    setTimeout(initVirtualSelect, 150);                    
    const resetBtn = document.querySelector('.btn-reset');
    if (resetBtn) {
        resetBtn.addEventListener('click', function() {
            const vs = document.querySelector('#districtSelect');
            if (vs && vs.reset) vs.reset();
        });
    }
});
