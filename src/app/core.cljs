(ns app.core
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            ["mapbox-gl" :as mapbox-gl]
            ["react" :refer [useRef useEffect useState]]
            [reagent.dom :as rdom]
            [app.components.mapbox :refer [map-box]]
            [app.integrations.mapbox]
            [app.spec]
            [app.bliss]))




(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (rdom/render [map-box] (.getElementById js/document "app")))

(defn ^:export main
  "Run application startup logic."
  []
  (render))
