(ns offcourse.appstate.model
  (:require [offcourse.models.action :refer [respond]]
            [offcourse.models.course :as co]
            [offcourse.models.checkpoint :as cp]
            [offcourse.appstate.viewmodel :as vm]))

(defrecord AppState [level mode course-collections viewmodel])

(defn new-appstate []
  (map->AppState {:level       {:type :initial}
                  :mode        :learn
                  :viewmodel   {}}))

(defn set-viewmodel [appstate viewmodel]
  (assoc-in appstate [:viewmodel] viewmodel))

(defn update-viewmodel [appstate fn]
  (update-in appstate [:viewmodel] fn))

(defn set-mode [appstate mode]
  (assoc-in appstate [:mode] mode))

(defn set-level [appstate level]
  (assoc appstate :level level))

(defn toggle-mode [appstate]
  (update-in appstate [:mode]
             #(if (= %1 :learn) :curate :learn)))

(defn add-checkpoint [appstate course]
  (let [checkpoint (cp/temp-checkpoint)
        course (co/add-temp-checkpoint course checkpoint)]
    (set-viewmodel appstate (vm/new-checkpoint course (:id checkpoint)))))

(defn refresh-checkpoint [{:keys [level] :as appstate} course]
  (let  [checkpoint-id (:checkpoint-id level)]
   (set-viewmodel appstate (vm/new-checkpoint course checkpoint-id))))

(defn update-checkpoint [{:keys [level] :as appstate} course]
  (let  [checkpoint-id (:checkpoint-id level)]
    (if (= :new checkpoint-id)
      (add-checkpoint appstate course)
      (refresh-checkpoint appstate course))))

(defn refresh-collection [{:keys [level] :as appstate} collection]
  (let [collection-name (:collection-name level)]
    (set-viewmodel appstate (vm/new-collection collection-name collection))))

(defn refresh-course [appstate course]
    (assoc-in appstate [:viewmodel] (vm/new-course course)))

(defn highlight-collection [appstate {:keys [course-id checkpoint-id highlight]}]
  (update-viewmodel appstate
                    #(vm/highlight-collection %1 course-id checkpoint-id highlight)))

(defn highlight-course [appstate {:keys [checkpoint-id highlight]}]
  (update-viewmodel appstate
                    #(vm/highlight-course %1 checkpoint-id highlight)))
