// Search Button
let input = document.querySelector(".input-btn");
let btn = document.querySelector(".btn-header");
let parent = document.querySelector(".search-btn");

btn.addEventListener("click", (event) => {
    event.stopPropagation();            		// Ngăn chặn sự kiện click từ việc lan ra các phần tử cha
    parent.classList.toggle("active");
    input.focus();
});
document.addEventListener("click", (event) => {
    if (!parent.contains(event.target)) {       // Kiểm tra xem sự kiện click có xảy ra trong phạm vi của parent hay không
        parent.classList.remove("active");      // Nếu không, loại bỏ lớp "active" khỏi parent
    }
});

//Scroll Top Button Click 
(function () {
    "use strict";
    /** Easy selector helper function */
    const select = (el, all = false) => {
        el = el.trim()
        if (all) {
            return [...document.querySelectorAll(el)]
        } else {
            return document.querySelector(el)
        }
    }

    /** Easy event listener function */
    const on = (type, el, listener, all = false) => {
        let selectEl = select(el, all)
        if (selectEl) {
            if (all) {
                selectEl.forEach(e => e.addEventListener(type, listener))
            } else {
                selectEl.addEventListener(type, listener)
            }
        }
    }

    /** Easy on scroll event listener */
    const onscroll = (el, listener) => {
        el.addEventListener('scroll', listener)
    }

    /** Scrolls to an element with header offset */
    const scrollto = (el) => {
        let header = select('#header')
        let offset = header.offsetHeight

        if (!header.classList.contains('header-scrolled')) {
            offset -= 20
        }

        let elementPos = select(el).offsetTop
        window.scrollTo({
            top: elementPos - offset,
            behavior: 'smooth'
        })
    }

    /** Toggle .header-scrolled class to #header when page is scrolled */
    let selectHeader = select('#header')
    if (selectHeader) {
        const headerScrolled = () => {
            if (window.scrollY > 100) {
                selectHeader.classList.add('header-scrolled')
            } else {
                selectHeader.classList.remove('header-scrolled')
            }
        }
        window.addEventListener('load', headerScrolled)
        onscroll(document, headerScrolled)
    }

    /** Back to top button */
    let backtotop = select('.back-to-top')
    if (backtotop) {
        const toggleBacktotop = () => {
            if (window.scrollY > 100) {
                backtotop.classList.add('active')
            } else {
                backtotop.classList.remove('active')
            }
        }
        window.addEventListener('load', toggleBacktotop)
        onscroll(document, toggleBacktotop)
    }
})()

