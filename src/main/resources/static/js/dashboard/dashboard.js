const DASHBOARD_API_BASE_URL = "/api/v1/dashboard";

let monthlyChart;
let weekdayChart;
let ageGroupChart;
let districtMap;
let districtGeoJsonLayer;
let rentTypeChart;

document.addEventListener("DOMContentLoaded", async function() {
    try {
        await loadKpi();
        await loadMonthlyChart();
        await loadWeekdayChart();
        await loadAgeGroupChart();
        await loadDistrictMap();
        await loadRentTypeChart();
    } catch (e) {
        console.error("대시보드 로딩 실패", e);
        alert("대시보드 데이터를 불러오지 못했습니다.");
    }
});

function xhrGet(url) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.onload = () => {
            if (xhr.status >= 200 && xhr.status < 300) {
                resolve(JSON.parse(xhr.responseText));
            } else {
                reject(new Error("XHR 실패: " + url));
            }
        };
        xhr.onerror = () => reject(new Error("네트워크 오류"));
        xhr.send();
    });
}

async function loadKpi() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/kpi`);

    document.getElementById("totalUseCnt").textContent = formatNumber(data.totalUseCnt);
    document.getElementById("totalCarbonSaved").textContent = formatDecimal(data.totalCarbonSaved);
    document.getElementById("totalStationCnt").textContent = formatNumber(data.totalStationCnt);
    document.getElementById("avgUseTime").textContent = formatDecimal(data.avgUseTime);
    document.getElementById("topDistrict").textContent = data.topDistrict ?? "-";
}

async function loadMonthlyChart() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/monthly-summary`);
    const ctx = document.getElementById("monthlyChart");

    if (monthlyChart) {
        monthlyChart.destroy();
    }

    monthlyChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: data.monthList,
            datasets: [
                {
                    label: "이용건수",
                    data: data.useCountList,
                    yAxisID: "y"
                },
                {
                    label: "대여소 수",
                    data: data.stationCountList,
                    type: "line",
                    yAxisID: "y1"
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: {
                mode: "index",
                intersect: false
            },
            scales: {
                y: {
                    beginAtZero: true,
                    position: "left"
                },
                y1: {
                    beginAtZero: true,
                    position: "right",
                    grid: {
                        drawOnChartArea: false
                    }
                }
            }
        }
    });
}

async function loadWeekdayChart() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/weekday-summary`);
    const ctx = document.getElementById("weekdayChart");

    if (weekdayChart) {
        weekdayChart.destroy();
    }

    weekdayChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: data.weekdayList,
            datasets: [
                {
                    label: "이용건수",
                    data: data.useCountList
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

async function loadAgeGroupChart() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/age-group-summary`);
    const ctx = document.getElementById("ageGroupChart");

    if (ageGroupChart) {
        ageGroupChart.destroy();
    }

    ageGroupChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: data.ageGroupList,
            datasets: [
                {
                    label: "이용건수",
                    data: data.useCountList
                }
            ]
        },
        options: {
            responsive: true,
            cutout: '55%',
            plugins: {
                legend: { position: 'right' }
            }
        }
    });
}

async function loadDistrictMap() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/district-summary`);

    const districtUsageMap = {};
    data.districtList.forEach((district, index) => {
        districtUsageMap[district] = data.useCountList[index];
    });

    const values = Object.values(districtUsageMap);
    const min = Math.min(...values);
    const max = Math.max(...values);

    if (districtMap) {
        districtMap.remove();
    }

    const seoulBounds = [
        [37.413, 126.734],
        [37.715, 127.269],
    ];

    districtMap = L.map("districtMap", {
        maxBounds: seoulBounds,
        maxBoundsViscosity: 1.0,
        minZoom: 10,
        maxZoom: 11,
        zoomControl: true,
        attributionControl: false
    }).setView([37.5665, 126.9780], 11);

    const geoData = await xhrGet("/geojson/seoul_districts.geojson");

    districtGeoJsonLayer = L.geoJSON(geoData, {
        style: function(feature) {
            const districtName = getDistrictName(feature);
            const value = districtUsageMap[districtName];

            return {
                fillColor: getColor(value, min, max),
                weight: 2,
                color: "#ffffff",
                fillOpacity: 0.85
            };
        },
        onEachFeature: function(feature, layer) {
            const districtName = getDistrictName(feature);
            const value = districtUsageMap[districtName] ?? 0;

            layer.bindTooltip(
                `<strong>${districtName}</strong><br>이용건수: ${formatNumber(value)}건`,
                { sticky: true }
            );

            layer.on({
                mouseover: function(e) {
                    e.target.setStyle({
                        weight: 3,
                        color: "#111827",
                        fillOpacity: 0.95
                    });
                    e.target.bringToFront();
                },
                mouseout: function(e) {
                    districtGeoJsonLayer.resetStyle(e.target);
                }
            });
        }
    }).addTo(districtMap);

    districtMap.fitBounds(districtGeoJsonLayer.getBounds());

    setTimeout(() => {
        districtMap.invalidateSize();
    }, 300);
}

async function loadRentTypeChart() {
    const data = await xhrGet(`${DASHBOARD_API_BASE_URL}/rent-type-summary`);
    const ctx = document.getElementById("rentTypeChart");

    if (rentTypeChart) {
        rentTypeChart.destroy();
    }

    rentTypeChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: data.rentTypeList,
            datasets: [
                {
                    label: "이용건수",
                    data: data.useCountList
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function formatNumber(value) {
    if (value == null) {
        return "-";
    }
    return new Intl.NumberFormat("ko-KR").format(value);
}

function formatDecimal(value) {
    if (value == null) {
        return "-";
    }
    return new Intl.NumberFormat("ko-KR", {
        maximumFractionDigits: 2
    }).format(value);
}

function getDistrictName(feature) {
    return (
        feature.properties.SIG_KOR_NM ||
        feature.properties.name ||
        feature.properties.SIGUNGU_NM ||
        feature.properties.sigungu_nm ||
        ""
    );
}

function getColor(value, min, max) {
    if (value == null) {
        return "#e5e7eb";
    }

    const ratio = (value - min) / ((max - min) || 1);

    if (ratio > 0.85) return "#0b3c8c";
    if (ratio > 0.65) return "#2563eb";
    if (ratio > 0.45) return "#60a5fa";
    if (ratio > 0.25) return "#93c5fd";
    return "#dbeafe";
}