(ns offcourse.routes
  (:require [clojure.string :as string]
            [offcourse.actions.index :as actions]
            [secretary.core :as secretary
             :include-macros true
             :refer-macros [defroute]]
            [offcourse.models.action :refer [>>!]]))

(def route-names {:home       "/"
                  :checkpoint "/courses/:course-id/checkpoints/:checkpoint-id"
                  :course     "/courses/:course-id"
                  :collection "/:collection-name"})

(def arguments {:checkpoint [:course-id :checkpoint-id]
                :collection [:collection-name]
                :course     [:course-id]})

(defn update-vals [vals f map]
  (reduce #(update-in % [%2] f) map vals))

(defn convertRouteParams [id]
  (cond
    (= "new" id) (keyword id)
    (= nil id) nil
    :default (js/parseInt id)))

(defn response [channel type & args]
  (let [payload (->> args
                     (zipmap (type arguments))
                     (update-vals [:course-id :checkpoint-id] convertRouteParams)
                     (into {:type type}))]
    (>>! channel :requested-resource
                 :payload payload)))

(defn init [{output :channel-out}]
  (let [response (partial response output)]

    (defroute (:checkpoint route-names) {course-id :course-id checkpoint-id :checkpoint-id}
      (response :checkpoint course-id checkpoint-id))

    (defroute (:course route-names) {course-id :course-id}
      (response :course course-id))

    (defroute (:collection route-names) {collection-name :collection-name}
      (response :collection (keyword collection-name)))

    (defroute (:home route-names) []
      (response :collection :featured))))
