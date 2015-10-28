(ns offcourse.views.components.card
  (:require [quiescent.dom :as d]
            [offcourse.views.components.todo-list :refer [TodoList]]
            [offcourse.helpers.css :as css]))


(comment (defn browse-course-button [{on-click :on-click}]
           [:div.btn.btn-inverse.browse
            {:on-click #(on-click)} "Open"])

         (defn browse-checkpoint-button [{on-click :on-click}]
           [:div.btn.btn-inverse.browse
            {:on-click #(on-click)} "Open"]))

(defn Button
  ([course-id {:keys [go-to-course]}]
   (d/div {:className "btn btn-inverse browse"
           :onClick #(go-to-course course-id)} "Open"))
  ([course-id checkpoint-id {:keys [go-to-checkpoint]}]
   (d/div {:className "btn btn-inverse browse"
           :onClick #(go-to-checkpoint course-id checkpoint-id)} "Open")))

(defn AddCheckpointButton [course-id {:keys [go-to-checkpoint]}]
   (d/div {:className "btn btn-inverse browse"
           :onClick #(go-to-checkpoint course-id "new")} "Add Checkpoint"))

(defn CommitCheckpointButton [course-id checkpoint-id {:keys [commit-checkpoint]}]
  (when (= checkpoint-id :new)
    (d/div {:className "btn btn-inverse browse"
            :onClick #(commit-checkpoint course-id (if checkpoint-id checkpoint-id :new))}
           "Add To Course")))

(defn Title [title]
  (d/h1 {} title))

(defn Map []
  (d/div))

(defn Checkbox [course-id checkbox-id completed? {:keys [toggle-done]}]
  (let [completed (if completed? "complete" "incomplete")]
    (d/div {:className completed
            :onClick #(toggle-done course-id checkbox-id)})))

(defn CardSection [index [type data-key] item handlers]
  (d/div {:key index
          :className (css/type-classes "card_section" type)}
         (case type
           :map (Map)
           :checkbox (Checkbox (:course-id item) (:id item ) (data-key item) handlers)
           :title (Title (data-key item))
           :info (Title (data-key (:resource item)))
           :list (TodoList (:id item) (data-key item) handlers)
           :course-button (Button (data-key item) handlers)
           :checkpoint-button (Button (:course-id item) (data-key item) handlers)
           :add-checkpoint-button (AddCheckpointButton (data-key item) handlers)
           :commit-checkpoint-button (CommitCheckpointButton (:course-id item) (data-key item) handlers))))

(defn Card [schema item handlers]
  (let [highlighted (if (:highlighted item) "highlighted" "not-highlighted")]
        (d/section {:key (:id item)
                    :className (css/classes "card" highlighted)}
                   (map-indexed #(CardSection %1 %2 item handlers) schema))))
