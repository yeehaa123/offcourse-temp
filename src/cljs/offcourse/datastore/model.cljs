(ns offcourse.datastore.model
  (:require [offcourse.models.action :refer [respond]]
            [offcourse.models.collection :as cl]))

(defrecord DataStore [collections courses])

(defn new-datastore []
  (->DataStore {} {}))

(defn update-collections [store collection-name collection-ids]
  (assoc-in store [:collections collection-name] collection-ids))

(defn update-cache [store fn]
  (update-in store [:courses] fn))

(defn update-course [store course]
  (update-cache store #(cl/update-course %1 course)))

(defn update-courses [store courses]
  (update-cache store #(cl/update-courses %1 courses)))

(defn add-checkpoint [store course-id checkpoint]
  (update-cache store #(cl/add-checkpoint %1 course-id checkpoint)))

(defn toggle-done [store course-id checkpoint-id]
  (update-cache store #(cl/toggle-done %1 course-id checkpoint-id)))

(defn augment-checkpoint [store course-id checkpoint-id resource]
  (update-cache store #(cl/augment-checkpoint %1 course-id checkpoint-id resource)))

(defn find-course [store course-id]
  (cl/find-course (:courses store) course-id))
