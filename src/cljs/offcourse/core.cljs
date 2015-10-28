(ns offcourse.core
  (:require [offcourse.api.index :as api]
            [cljs.core.async :refer [merge pipeline chan mult tap]]
            [offcourse.views.index :as views]
            [offcourse.actions.index :as actions]
            [offcourse.appstate.index :as appstate]
            [offcourse.datastore.index :as datastore]
            [offcourse.logger.service :as logger]
            [offcourse.user.service :as user]
            [offcourse.routes :as router]
            [offcourse.history.service :as history]))

(defn init! []
  (let [actions-appstate   (chan)
        actions-log        (chan)
        actions-out        (chan)
        actions-out-mult   (mult actions-out)

        appstate-datastore (chan)
        appstate-log       (chan)
        appstate-out       (chan)
        appstate-out-mult  (mult appstate-out)

        user-in            (chan)
        user-out           (chan)

        datastore-appstate (chan)
        datastore-api      (chan)
        datastore-log      (chan)
        datastore-out      (chan)
        datastore-out-mult (mult datastore-out)

        api-datastore      (chan)
        api-log            (chan)
        api-out            (chan)
        api-out-mult       (mult api-out)

        router-out         (chan)
        router-log         (chan)
        router-appstate    (chan)
        router-out-mult    (mult router-out)

        history-in         (chan)

        views-in           (chan)
        views-appstate     (chan)
        views-log          (chan)
        views-out          (chan)
        views-out-mult     (mult views-out)

        appstate-in        (merge [router-appstate actions-appstate
                                   datastore-appstate views-appstate])
        datastore-in       (merge [appstate-datastore api-datastore])
        api-in             datastore-api
        logger-in          (merge [actions-log router-log api-log user-out appstate-log
                                   views-log datastore-log] 10)]

    (tap actions-out-mult actions-appstate)
    (tap actions-out-mult actions-log)

    (tap appstate-out-mult appstate-datastore)
    (tap appstate-out-mult history-in)
    (tap appstate-out-mult views-in)
    (tap appstate-out-mult appstate-log)

    (tap datastore-out-mult datastore-api)
    (tap datastore-out-mult datastore-appstate)
    (tap datastore-out-mult datastore-log)

    (tap api-out-mult api-datastore)
    (tap api-out-mult api-log)

    (tap router-out-mult router-log)
    (tap router-out-mult router-appstate)

    (tap views-out-mult views-appstate)
    (tap views-out-mult views-log)

    (actions/init        {:channel-out  actions-out})

    (router/init         {:channel-out  router-out})

    (appstate/init       {:channel-in   appstate-in
                          :channel-out  appstate-out})

    (user/init           {:channel-in   user-in
                          :channel-out  user-out})

    (datastore/init      {:channel-in   datastore-in
                          :channel-out  datastore-out})

    (api/init            {:channel-in   api-in
                          :channel-out  api-out})

    (history/init!       {:channel-in   history-in})


    (views/init          {:channel-in   views-in
                          :channel-out  views-out})

    (logger/init         {:channel-in   logger-in})))

(defn reload []
  (actions/refresh))
