(ns offcourse.datastore.helpers
  (:require [offcourse.models.action :refer [respond]]))

(defn init [store]
  (defn respond-updated []
    (respond :updated-data
             :store store))

  (defn respond-added [course-id]
    (respond :added-checkpoint
             :course-id course-id
             :store store))

  (defn respond-not-found [type {:keys [course-id course-ids collection-name]}]
    (case type
      :collection (respond :not-found-data
                           :type type
                           :collection-name collection-name
                           :store store)
      :courses    (respond :not-found-data
                           :type type
                           :course-ids course-ids
                           :store store)
      :course     (respond :not-found-data
                           :type type
                           :course-id course-id
                           :store store)))

  (defn respond-checked [type {:keys [course-id collection-name]}]
    (case type
      :collection (respond :checked-datastore
                           :type type
                           :collection-name collection-name
                           :store store)
      :course     (respond :checked-datastore
                           :type type
                           :course-id course-id
                           :store store)))
  (defn respond-ignore []
    respond :ignore))
