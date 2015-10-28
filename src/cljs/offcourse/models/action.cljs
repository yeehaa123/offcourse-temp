(ns offcourse.models.action
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [>!]]))

(defrecord Action [type payload])

(defn respond [type & payload]
  (let [args    (apply hash-map payload)
        payload (or (:payload args) args)]
    (map->Action {:type type
                  :payload payload})))

(defn >>! [channel & response]
  (go (>! channel (apply respond response))))

