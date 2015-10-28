(ns offcourse.models.course
  (:require [offcourse.models.checkpoint :as cp]))

(defrecord Course [id goal checkpoints])

(defn new-course [course id]
  (assoc course :id id))

(defn add-temp-checkpoint [course checkpoint]
  (assoc-in course [:checkpoints :new] checkpoint))

(defn next-checkpoint-id [course]
  (inc (apply max (keys (:checkpoints course)))))

(defn add-checkpoint [course checkpoint]
  (let [checkpoint (cp/new-checkpoint checkpoint (next-checkpoint-id course))]
    (assoc-in course [:checkpoints (:id checkpoint)] checkpoint)))

(defn find-checkpoint [course checkpoint-id]
  (get (:checkpoints course) checkpoint-id))

(defn needs-resources? [course]
  (not (every? :resource (vals (:checkpoints course)))))

(defn toggle-done [course checkpoint-id]
  (update-in course [:checkpoints checkpoint-id] #(cp/toggle-done %1)))

(defn augment-checkpoint [course checkpoint-id resource]
  (update-in course [:checkpoints checkpoint-id] #(cp/add-resource %1 resource)))

(defn highlight [course checkpoint-id highlight]
  (update-in course [:checkpoints checkpoint-id :highlighted] (fn [] highlight)))
