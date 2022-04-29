(ns app.integrations.mapbox
  "This namespace contains your application and is the entrypoint for 'yarn start'."
  (:require [reagent.core :as r]
            ["mapbox-gl" :as mapbox-gl]
            ["react" :refer [useRef]]
            [reagent.dom :as rdom]
            [app.spec]
            [app.config :refer [config]]))

;; set access token
(set! mapbox-gl -accessToken (-> config :mapbox :access-token))
(js/console.log mapbox-gl)
