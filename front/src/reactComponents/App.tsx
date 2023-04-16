import React, { useEffect, useRef, useState } from "react";
import "../styling/App.css";
import Map, {
  GeolocateControl,
  Layer,
  MapLayerMouseEvent,
  MapRef,
  NavigationControl,
  PointLike,
  Popup,
  Source,
  ViewStateChangeEvent,
} from "react-map-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { API_KEY } from "../private/api";
import {
  extractData,
  geoLayer,
  geoSearchLayer,
  OverlayResponse,
} from "../overlays";

/**
 * This interface is used to define the properties of the App component.
 */
interface AppProps {
  getMainOverlayResponse: () => Promise<OverlayResponse | undefined>;
  getSearchOverlayResponse: (
    keyword: string
  ) => Promise<OverlayResponse | undefined>;
}

/**

 The App component renders a map using the react-map-gl library, and allows the user to search for data
 overlays by typing into an input box. When the user clicks on the map, this component sends the corresponding
 latitude and longitude to the backend, and displays any resulting overlay data in a popup.
 @param {AppProps} props - an object containing two functions for retrieving overlay data
 @returns {JSX.Element} - a React component that renders a map and an input box for searching
 */
function App(props: AppProps) {
  const ProvidenceLat = 41.824;
  const ProvidenceLon = -71.4128;
  const initialZoom = 10;

  const [viewState, setViewState] = useState({
    longitude: ProvidenceLon,
    latitude: ProvidenceLat,
    zoom: initialZoom,
  });
  const [mainOverlay, setMainOverlay] = useState<
    GeoJSON.FeatureCollection | undefined
  >(undefined);
  const [searchOverlay, setSearchOverlay] = useState<
    GeoJSON.FeatureCollection | undefined
  >(undefined);
  const [textbox, setTextbox] = useState("");
  const mapRef = useRef<MapRef>(null);
  const [popupLat, setPopupLat] = useState(0);
  const [popupLon, setPopupLon] = useState(0);
  const [popupVisible, setPopupVisible] = useState(false);
  const [popupState, setPopupState] = useState<string[]>([]);
  const [popupCity, setPopupCity] = useState<string[]>([]);
  const [popupName, setPopupName] = useState<string[]>([]);
  const inputRef = useRef<HTMLInputElement>(null);

  /**
   * This useEffect hook renders any relevant overlay data upon each full render of the page.
   */
  useEffect(() => {
    extractData(props.getMainOverlayResponse())
      .then((result) => setMainOverlay(result))
      .catch((error) => console.error(error));
  }, []);

  /**
   * This function takes in a user mouse event on the map, and hands the latitude/longitude off to the back end.
   *
   * @param e - the mouse event cause by the user
   */
  function onMapClick(e: MapLayerMouseEvent) {
    setPopupLat(e.lngLat.lat);
    setPopupLon(e.lngLat.lng);
    const bbox: [PointLike, PointLike] = [
      [e.point.x - 5, e.point.y - 5],
      [e.point.x + 5, e.point.y + 5],
    ];

    // Find features intersecting the bounding box.
    const selectedFeatures =
      mapRef.current &&
      mapRef.current.queryRenderedFeatures &&
      mapRef.current.queryRenderedFeatures(bbox, {
        layers: ["geo_data"],
      });

    setPopupVisible(true);
    const uniqueStates = new Set<string>();
    const uniqueCities = new Set<string>();
    const uniqueNames = new Set<string>();

    if (selectedFeatures) {
      selectedFeatures.forEach((feature) => {
        if (feature.properties && feature.properties.state)
          uniqueStates.add(feature.properties.state);
        if (feature.properties && feature.properties.city)
          uniqueCities.add(feature.properties.city);
        if (feature.properties && feature.properties.name)
          uniqueNames.add(feature.properties.name);
      });
    }

    setPopupState(Array.from(uniqueStates));
    setPopupCity(Array.from(uniqueCities));
    setPopupName(Array.from(uniqueNames));
  }

  /**
   * This function handles the submission of the search box, and sends the user's input to the back end.
   */
  function handleSubmit() {
    extractData(props.getSearchOverlayResponse(textbox))
      .then((result) => setSearchOverlay(result))
      .catch((error) => console.error(error));
    setTextbox("");
  }

  /**
   * This effect handles the "/" key being pressed. It focuses the input box.
   */
  useEffect(() => {
    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "/" && document.activeElement !== inputRef.current) {
        event.preventDefault();
        if (inputRef.current !== null) {
          inputRef.current.focus();
        }
      }
    }

    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, []);

  return (
    <div className="App" style={{ position: "relative" }}>
      {/* Our interactive map element that uses the MapBox API */}
      <Map
        ref={mapRef}
        mapboxAccessToken={API_KEY}
        longitude={viewState.longitude}
        latitude={viewState.latitude}
        zoom={viewState.zoom}
        // or {...viewState} achieves the above 3
        onMove={(ev: ViewStateChangeEvent) => setViewState(ev.viewState)}
        // width/height defines how big the map is on the screen
        style={{ width: window.innerWidth, height: window.innerHeight }}
        mapStyle={"mapbox://styles/mapbox/dark-v10"}
        onClick={onMapClick}
        aria-label="Interactive Map Element"
      >
        <div
          style={{ position: "absolute", top: 0, right: 0, padding: "10px" }}
        >
          <NavigationControl
            visualizePitch={true}
            aria-label="Map Navigation Control"
          />
          <GeolocateControl
            positionOptions={{ enableHighAccuracy: true }}
            showUserLocation={true}
            fitBoundsOptions={{ maxZoom: 10 }}
            aria-label="Map Geolocate Control"
          />
        </div>

        <Source id="geo_data" type="geojson" data={mainOverlay}>
          {/* <- could also spread these props */}
          <Layer id={geoLayer.id} type={geoLayer.type} paint={geoLayer.paint} />
        </Source>
        <Source id="geo_search_data" type="geojson" data={searchOverlay}>
          {/* <- could also spread these props */}
          <Layer
            id={geoSearchLayer.id}
            type={geoSearchLayer.type}
            paint={geoSearchLayer.paint}
          />
        </Source>
        {popupVisible && (
          <Popup
            className="popup-container"
            latitude={popupLat}
            longitude={popupLon}
            closeButton={true}
            closeOnClick={false}
            onClose={() => setPopupVisible(false)}
            aria-label="Popup Information"
          >
            <div>
              <strong>State:</strong>{" "}
              {popupState.length > 0
                ? popupState.join(", ")
                : "Data not available"}
            </div>
            <div>
              <strong>City:</strong>{" "}
              {popupCity.length > 0
                ? popupCity.join(", ")
                : "Data not available"}
            </div>
            <div>
              <strong>Name:</strong>{" "}
              {popupName.length > 0
                ? popupName.join(", ")
                : "Data not available"}
            </div>
          </Popup>
        )}
      </Map>
      {/* Input box that overlays the map */}
      <input
        ref={inputRef}
        type="text"
        className="input-box"
        placeholder={"Input hole. Put it in here to search."}
        onChange={(e) => setTextbox(e.target.value)}
        value={textbox}
        onKeyUp={(e) => {
          if (e.key === "Enter") {
            handleSubmit();
          }
        }}
        role="textbox"
        aria-multiline="false"
        aria-label="Command Input Box"
      />
    </div>
  );
}

export default App;
