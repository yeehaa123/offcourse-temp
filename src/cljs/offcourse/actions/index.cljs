(ns offcourse.actions.index
  (:require [offcourse.models.action :refer [>>!]]))

(defn init [{output :channel-out}]
  (let [>>! (partial >>! output)]

    (defn refresh []
      (>>! :reloaded-code))))
