const DASHBOARD_API_BASE_URL = "/api/v1/dashboard";

let monthlyChart;
let weekdayChart;
let ageGroupChart;
let districtChart;

document.addEventListener("DOMContentLoaded", async function() {
    try {
        await loadKpi();
        await loadMonthlyChart();
        await loadWeekdayChart();
        await loadAgeGroupChart();
        await loadDistrictChart();
    } catch (e) {
        console.error("대시보드 로딩 실패", e);
        alert("대시보드 데이터를 불러오지 못했습니다.");
    }
});

async function fetchJson(url) {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("API 호출 실패: " + url);
    }

    return await response.json();
}

async function loadKpi() {
    const data = await fetchJson(`${DASHBOARD_API_BASE_URL}/kpi`);

    document.getElementById("totalUseCount").textContent = formatNumber(data.totalUseCount);
    document.getElementById("totalCarbonSaved").textContent = formatDecimal(data.totalCarbonSaved);
    document.getElementById("totalStationCount").textContent = formatNumber(data.totalStationCount);
    document.getElementById("avgUseTime").textContent = formatDecimal(data.avgUseTime);
    document.getElementById("topDistrict").textContent = data.topDistrict ?? "-";
}

async function loadMonthlyChart() {
    const data = await fetchJson(`${DASHBOARD_API_BASE_URL}/monthly-summary`);
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
    const data = await fetchJson(`${DASHBOARD_API_BASE_URL}/weekday-summary`);
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
    const data = await fetchJson(`${DASHBOARD_API_BASE_URL}/age-group-summary`);
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
            cutout: '55%' /* 구멍 크기 — 클수록 얇아짐 */,
            plugins: {
                legend: { position: 'right' },
            },
        }
    });
}

async function loadDistrictChart() {
    const data = await fetchJson(`${DASHBOARD_API_BASE_URL}/district-summary`);
    const ctx = document.getElementById("districtChart");

    if (districtChart) {
        districtChart.destroy();
    }

    districtChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: data.districtList,
            datasets: [
                {
                    label: "이용건수",
                    data: data.useCountList
                }
            ]
        },
        options: {
            indexAxis: "y",
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
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