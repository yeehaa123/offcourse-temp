(ns offcourse.logger.service
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [cljs.core.async :refer [>! <!]]
            [ajax.core :refer [PUT]]))


(def log-index (atom 0))

(defn store-logs [action]
  (let [url (str "https://cth-logs.firebaseio.com/" (.getTime (js/Date.)) ".json")
        payload (:payload action)]
    (swap! log-index inc)
    (PUT url {:format :json
              :keywords? true
              :params {:type (:type action)
                       :collection-name (:collection-name payload)
                       :course-id (:course-id payload)
                       :location (:location payload)
                       :ip (:ip payload)
                       :checkpoint-id (:checkpoint-id payload)}})))

(defn listen-for-actions [{input :channel-in}]
  (go-loop []
    (let [{type :type :as action} (<! input)]
      (println type)
      ;; (store-logs action)
      (recur))))

(defn init [config]
  (listen-for-actions config))
