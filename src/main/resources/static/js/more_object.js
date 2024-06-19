// Transport - Click lựa chọn các Object Transport
document.addEventListener("DOMContentLoaded", function() {	//tải trang xong thì mới sử dụng JS
	const query = document.querySelector.bind(document);
	const queryAll = document.querySelectorAll.bind(document);
	const tabs = queryAll(".tab-item");
	const panes = queryAll(".tab-pane");
	const tabActive = query(".tab-item.active");
	const line = query(".tabs .line");

	requestIdleCallback(function() {
		line.style.left = tabActive.offsetLeft + "px";
		line.style.width = tabActive.offsetWidth + "px";
	});
	tabs.forEach((tab, index) => {
		const pane = panes[index];

		tab.onclick = function() {
			query(".tab-item.active").classList.remove("active");
			query(".tab-pane.active").classList.remove("active");

			line.style.left = this.offsetLeft + "px";
			line.style.width = this.offsetWidth + "px";

			this.classList.add("active");
			pane.classList.add("active");
		};
	});
});