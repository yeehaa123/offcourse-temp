(ns offcourse.datastore.store
  (:require [offcourse.datastore.model :as model]
            [offcourse.models.course :as c]
            [clojure.set :as set]
            [offcourse.models.action :refer [respond]]
            [offcourse.datastore.helpers :as helpers]))

(def store (atom (model/new-datastore)))

(helpers/init store)

(defn- update-datastore! [fn]
  (swap! store fn))

(defn- update-and-respond! [fn]
  (do
    (update-datastore! fn)
    (helpers/respond-updated)))

(defn- add-and-respond! [fn course-id]
  (do
    (update-datastore! fn)
    (helpers/respond-added course-id)))

(defn- update-collections [{:keys [collection-name collection-ids]}]
  (let [courses-ids (into #{} (keys (:courses @store)))
        missing-ids (set/difference collection-ids courses-ids)]
    (if (empty? missing-ids)
      (update-and-respond! #(model/update-collections %1 collection-name collection-ids))
      (do
        (update-datastore! #(model/update-collections %1 collection-name collection-ids))
        (helpers/respond-not-found :courses {:course-ids missing-ids})))))

(defn- update-course [{:keys [course]}]
  (update-and-respond! #(model/update-course %1 course)))

(defn- update-courses [{:keys [courses]}]
  (update-and-respond! #(model/update-courses %1 courses)))

(defn- toggle-done [{:keys [course-id checkpoint-id]}]
  (update-and-respond! #(model/toggle-done %1 course-id checkpoint-id)))

(defn- augment-checkpoint [{:keys [course-id checkpoint-id resource]}]
  (update-and-respond! #(model/augment-checkpoint %1 course-id checkpoint-id resource)))

(defn- add-checkpoint [{:keys [course-id checkpoint]}]
  (let [course (model/find-course @store course-id)]
    (add-and-respond! #(model/add-checkpoint %1 course-id checkpoint) course-id)))

(defn- save-checkpoint [{:keys [checkpoint-id] :as payload}]
  (if (= checkpoint-id :new)
    (add-checkpoint payload)
    (helpers/respond-ignore)))

(defn- get-collection [{:keys [collection-name]}]
  (let [collections (:collections @store)]
    (if (collection-name collections)
      (helpers/respond-checked :collection {:collection-name collection-name})
      (helpers/respond-not-found :collection {:collection-name collection-name}))))

(defn- get-course [{:keys [course-id]}]
  (let [course (model/find-course @store course-id)]
    (if course
      (helpers/respond-checked :course {:course-id course-id})
      (helpers/respond-not-found :course {:course-id course-id}))))

;; Public API

(defn commit-data [{type :type :as payload}]
  (case type
    :checkpoint (save-checkpoint payload)))

(defn get-data [{type :type :as payload}]
  (case type
    :collection (get-collection payload)
    :course     (get-course payload)
    :checkpoint (get-course payload)))

(defn update-datastore [{:keys [type] :as payload}]
  (case type
    :collection (update-collections payload)
    :course     (update-course payload)
    :courses    (update-courses payload)
    :resource   (augment-checkpoint payload)))
