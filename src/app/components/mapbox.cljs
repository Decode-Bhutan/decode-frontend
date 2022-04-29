(ns app.components.mapbox
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            ["mapbox-gl" :as mapbox-gl]
            ["react" :refer [useRef useEffect useState]]
            [reagent.dom :as rdom]
            [app.integrations.mapbox]
            [app.spec]))

(def map-styles
  {:digital "mapbox://styles/mapbox/streets-v11"
   :satellite "mapbox://styles/mapbox-map-design/ckhqrf2tz0dt119ny6azh975y"})

(defonce saved-locations (r/atom []))


(def mapbox-map (.-Map mapbox-gl))

(def state (atom nil))
(comment
  (js/console.log @state)
  (set! js/window -state @state))
(defonce tennis-ground {:lng 89.6410894349446,
                        :lat 27.472543144889357})
(defonce lungtenzampa {:lat 27.46836002360513, :lng 89.64400769188558})
(defonce buddah-point {:lat 27.443419684442063, :lng 89.64567281493953})
(defonce phnom-penh {:lat 11.560267443771764, :lng 104.89675851689418})
(defonce paro-airport {:lat 27.398172899683473, :lng 89.42539498843189})
(defonce base-camp {:lat 28.00429596068247, :lng 86.85711562306801})
(defonce everest {:lat 27.98764911516463, :lng 86.9246515901267})

(defonce places (r/atom {:tennis-ground tennis-ground}))
(defonce current-place (r/atom tennis-ground))

(comment
  @current-place)

(comment
  @state)


(defn map-box []
  (let [map-container (atom nil)]

        ; ref (atom nil)]
   (r/create-class
     {:component-did-mount
      (fn []
        (let [map-box (new mapbox-map (clj->js {:container @map-container
                                                ; :style "mapbox://styles/mapbox/streets-v11"
                                                :style (:satellite map-styles)
                                                :center [(:lng @current-place), (:lat @current-place)]
                                                :zoom 18
                                                :pitch 85,
                                                :bearing 80,}))]
          (set! js/window -state map-box)
          (reset! state map-box)
          (.on map-box
               "load"
               (fn []
                 (.addSource map-box "mapbox-dem" (clj->js {:type "raster-dem",
                                                            :url "mapbox://mapbox.mapbox-terrain-dem-v1",
                                                            :tileSize 512
                                                            :maxzoom 14}))
                 (.setTerrain map-box (clj->js  {:source  "mapbox-dem"
                                                 :exaggeration 1.5}))
                 (.addLayer map-box (:clj->js {:id "sky"
                                               :type "sky"
                                               :paint {:sky-type "atmosphere"
                                                       :sky-atmosphere-sun  [0.0, 0.0],
                                                       :sky-atmosphere-sun-intensity 15}}))))

                                ;;


          (.on map-box
               "move"
               (fn []
                 (reset! current-place {:lat (.-lat (.getCenter map-box))
                                        :lng (.-lng (.getCenter map-box))})))))
                 ; (when-not (.getLayer map-box "terrain-data")
                 ;   (.addLayer map-box (clj->js {:id "terrain-data"
                 ;                                :type "line",
                 ;                                :source {:type "vector",
                 ;                                         :url "mapbox://mapbox.mapbox-terrain-v2"}
                 ;                                :source-layer "contour"})))))))
                              ;

      :reagent-render
      (fn []
       [:div
        [:h1 "Decode bhutan"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js buddah-point)))}
                    ; (.setZoom @state 18))}
          "Buddah Point"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js tennis-ground)))}
                    ; (.setZoom @state 18))}
          "Tennis Ground"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js lungtenzampa)))}
                    ; (.setZoom @state 18))}
          "Old basketball court"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js phnom-penh)))}
                    ; (.setZoom @state 18))}
          "Phnom Penh"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js paro-airport)))}
                    ; (.setZoom @state 18))}
          "Paro Airport"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js base-camp)))}
                    ; (.setZoom @state 18))}
          "Everest Basecamp"]
        [:button {:on-click
                  (fn []
                    (.setCenter @state (clj->js everest)))}
                    ; (.setZoom @state 18))}
          "Everest"]
        [:div
         [:button {:on-click #(.setStyle @state (:satellite map-styles))} "Satellite"]
         [:button {:on-click #(.setStyle @state (:digital map-styles))} "Digital"]]
        [:div.map-container {:ref (fn [el]
                                    (reset! map-container el))}]])})))



#_(defn map-box []
    (let [map-container (useRef)
          ref (useRef)
          [current-location set-current-place] (useState (clj->js tennis-ground))
          [lat set-lat] (useState (:lat tennis-ground))
          [lng set-lng] (useState (:lng tennis-ground))
          ; current-location @current-place
          _  (useEffect (fn []
                          (when-not (.-current ref)
                            (reset! state (.-current map-container))
                            (set! ref -current (new mapbox-map (clj->js {:container (.-current map-container)
                                                                         :style "mapbox://styles/mapbox/streets-v11"
                                                                         :center [lng,lat]
                                                                         :zoom 20}))))))
          _  (useEffect (fn []
                          (when-let [map-box (.-current ref)]
                            (.on map-box "move" (fn []
                                                  (js/console.log (.getCenter map-box))
                                                  (set-lat (.-lat (.getCenter map-box)))))))

                        (clj->js [(.-current ref)]))]
      (fn []
        [:div
         [:h1 "Decode bhutan"]
         [:div.map-container {:ref map-container}]])))
