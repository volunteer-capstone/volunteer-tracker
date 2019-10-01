const defaultOptions = {
    initialSlide: 0,
    slidesToScroll: 1,
    slidesToShow: 1,

    navigation: true,
    navigationKeys: true,
    navigationSwipe: true,

    pagination: true,

    loop: true,
    infinite: false,

    effect: 'translate',
    duration: 300,
    timing: 'ease',

    autoplay: true,
    autoplaySpeed: 3000,
    pauseOnHover: true,
    breakpoints: [{
        changePoint: 480,
        slidesToShow: 1,
        slidesToScroll: 1
    },
        {
            changePoint: 640,
            slidesToShow: 2,
            slidesToScroll: 2
        },
        {
            changePoint: 768,
            slidesToShow: 3,
            slidesToScroll: 3
        }
    ],

    onReady: null

};

// Initialize all elements with carousel class.
const carousels = bulmaCarousel.attach('.carousel', options);

// To access to bulmaCarousel instance of an element
const element = document.querySelector('#my-element');
if (element && element.bulmaCarousel) {
    // bulmaCarousel instance is available as element.bulmaCarousel
}

bulmaCarousel.attach('#carousel-demo', {
    slidesToScroll: 1,
    slidesToShow: 4
});

