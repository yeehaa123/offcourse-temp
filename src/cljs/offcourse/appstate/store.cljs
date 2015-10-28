(ns offcourse.appstate.store
  (:require [offcourse.appstate.model :as model]
            [offcourse.appstate.utils :as utils]
            [offcourse.models.course :as co]
            [offcourse.models.action :refer [respond]]))

(def appstate (atom (model/new-appstate)))

(defn- update-appstate! [fn]
  (do
    (swap! appstate fn)
    (respond :updated-appstate
             :appstate @appstate)))

(defn- refresh-checkpoint [{store :store}]
  (let [level (:level @appstate)
        course (utils/get-course @appstate @store)
        checkpoint-id (:checkpoint-id (:level @appstate))
        checkpoint (co/find-checkpoint course checkpoint-id)]
    (if (or checkpoint (= checkpoint-id :new))
      (update-appstate! #(model/update-checkpoint %1 course))
      (respond :not-found-resource))))

(defn- refresh-collection [{:keys [store]}]
  (let [collection (utils/get-collection @appstate @store)]
    (update-appstate! #(model/refresh-collection %1 collection))))

(defn- refresh-course [{:keys [store]}]
  (let [course (utils/get-course @appstate @store)]
    (if course
      (update-appstate! #(model/refresh-course %1 course))
      (respond :not-found-resource))))

;; Public API

(defn set-mode [{mode :mode}]
  (update-appstate! #(model/set-mode %1 mode)))

(defn toggle-mode []
  (update-appstate! #(model/toggle-mode %1)))

(defn set-level [payload]
  (update-appstate! #(model/set-level %1 payload)))

(defn refresh [payload]
  (let [{type :type :as level} (:level @appstate)]
    (case type
      :collection (refresh-collection payload)
      :course (refresh-course payload)
      :checkpoint (refresh-checkpoint payload))))

(defn toggle-highlight [payload]
  (let [{type :type :as level} (:level @appstate)]
    (case type
      :collection (update-appstate! #(model/highlight-collection %1 payload))
      :course (update-appstate! #(model/highlight-course %1 payload)))))

(defn commit-data [{:keys [course-id checkpoint-id] :as payload}]
  (let [course (:course (:viewmodel @appstate))
        checkpoint (co/find-checkpoint course checkpoint-id)
        payload (assoc payload :course-id course-id
                               :checkpoint checkpoint)]
    (respond :requested-commit
             :payload payload)))

(defn force-refresh []
  (respond :reloaded-appstate
           :appstate @appstate))
