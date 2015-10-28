(ns offcourse.api.index
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [cljs.core.async :refer [timeout <! >!]]
            [offcourse.api.service :as service :refer [internal]]))

(defn listen-for-actions [{input :channel-in
                           output :channel-out}]
  (go-loop []
    (let [[{type :type payload :payload}] (alts! [input internal])]
      (println type)
      (case type
        :not-found-data       (>! output (service/find-data payload))
        :requested-data       (>! output (service/fetch-resource payload))
        :checked-datastore    (>! output (service/fetch-updates payload))
        nil))
    (recur)))

(defn init [config]
  (listen-for-actions config))
