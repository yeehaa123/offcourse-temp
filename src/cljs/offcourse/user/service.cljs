(ns offcourse.user.service
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :refer [>! chan alts!]]
            [ajax.core :refer [GET]]
            [offcourse.models.action :refer [respond]]))

(def user (atom {:ip nil
                 :location {}}))

(defn listen-for-actions [{input :channel-in}]
  (go-loop []
    (let [{type :type payload :payload} (<! input)]
      (println payload))
    (recur)))

(defn handle-location [channel position]
  (go
    (let [coords {:longitude (.-longitude js/position.coords)
                  :latitude (.-latitude js/position.coords)}]
      (>! channel (respond :found-location
                           :location coords)))))

(defn handle-ip [channel response]
  (go
    (>! channel (respond :found-ip
                         :ip (:ip response)))))

(defn init [{output :channel-out :as config}]
  (let [handle-ip (partial handle-ip output)
        handle-location (partial handle-location output)]
    (do
      (listen-for-actions config)
      (GET "https://api.ipify.org?format=json" {:response-format :json
                                                :keywords? true
                                                :handler handle-ip})
      (.getCurrentPosition js/navigator.geolocation handle-location))))
