(ns offcourse.views.components.breadcrumbs
  (:require [clojure.string :as string]
            [quiescent.dom :as d]))

(defn Breadcrumb [crumb {on-click :go-to}]
  (let [title (as-> crumb $
                (:title $)
                (name $)
                (string/split $ #" ")
                (map string/capitalize $)
                (string/join " " $))
        link? (:link crumb)
        options {:key (:level crumb)
                 :className "btn btn-level"}
        options (if link?
                  (assoc options :onClick #(on-click crumb))
                  options)]
      (d/li options title)))

(defn createCrumbs [{:keys [level collection-name course checkpoint-id]}]
  (case level
    :collection [{:level level
                  :title collection-name
                  :collection-name collection-name}]
    :course     [{:level level
                  :title (:goal course)
                  :course-id (:id course)}]
    :checkpoint [{:level :course
                  :title (:goal course)
                  :course-id (:id course)
                  :link true}
                 {:level level
                  :title (:task (get-in course [:checkpoints checkpoint-id]))
                  :checkpoint-id checkpoint-id}]))

(defn Breadcrumbs [viewmodel handlers]
  (let [crumbs (createCrumbs viewmodel)]
  (d/nav {:className "breadcrumbs"}
         (d/ul {}
               (for [crumb crumbs]
                 (Breadcrumb crumb handlers))))))


