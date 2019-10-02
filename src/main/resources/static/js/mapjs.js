"use strict";
$(document).ready(function() {
    let mapToken=$('#mapToken').val();

    mapboxgl.accessToken = mapToken;
    let eventLocation = $('#address').text();

    geocode(eventLocation, mapToken).then(function(loc) {
        let mapOptions = {
            container: 'map',
            style: 'mapbox://styles/mapbox/streets-v9',
            zoom: 15,
            showZoom: true,
            center: loc
        };

        let map = new mapboxgl.Map(mapOptions);

        let marker = new mapboxgl.Marker()
            .setLngLat(loc)
            .addTo(map);

        map.addControl(new mapboxgl.NavigationControl());

    });
});
