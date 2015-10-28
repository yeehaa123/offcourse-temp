(ns offcourse.views.actions
  (:require [offcourse.models.action :refer [>>!]]))

(defn init [{output :channel-out}]
  (let [>>! (partial >>! output)]

    {:set-mode         (fn [mode]
                         (>>! :requested-mode-switch
                              :mode mode))

     :toggle-mode      (fn []
                         (>>! :requested-mode-toggle))

     :toggle-done      (fn [course-id checkpoint-id]
                         (>>! :requested-done-toggle
                              :course-id course-id
                              :checkpoint-id checkpoint-id))

     :highlight       (fn [course-id checkpoint-id highlight]
                        (>>! :requested-highlight-toggle
                             :course-id course-id
                             :checkpoint-id checkpoint-id
                             :highlight highlight))

     :go-to-collection (fn [collection-name]
                         (>>! :requested-level
                              :level :collection
                              :collection-name collection-name))

     :go-to-course     (fn [course-id]
                         (>>! :requested-level
                              :level :course
                              :course-id course-id))

     :go-to-checkpoint (fn [course-id checkpoint-id]
                         (>>! :requested-level
                              :level :checkpoint
                              :course-id course-id
                              :checkpoint-id checkpoint-id))

     :commit-checkpoint (fn [course-id checkpoint-id]
                          (>>! :requested-commit
                               :type :checkpoint
                               :course-id course-id
                               :checkpoint-id checkpoint-id))

     :go-to            (fn [payload]
                         (>>! :requested-level
                              :payload payload))}))
