(ns real-world.frontend.core
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]))

(defonce match (r/atom nil))

(defn page-name []
  (-> @match :data :name))

(defn nav-item [{:keys [name]} & children]
  (let [active? (= (page-name) name)]
   [:li.nav-item
    {:class (when active? :active)}
    [:a.nav-link
     {:href (rfe/href name)}
     children]]))

(defn header [children]
  [:div
   [:nav.navbar.navbar-light
    [:div.container
     [:a.navbar-brand
      {:href (rfe/href ::home)}
      "conduit"]
     [:ul.nav.navbar-nav.pull-xs-right
      [nav-item
       {:name ::home}
       "Home"]
      [nav-item
       {:name ::about}
       [:i.ion-gear-a]
        "Settings"]      
      [:li.nav-item
       [:a.nav-link
        "Sign Up"]]]]]
   [:div
    children]])

(defn current-page []
  [header
   (if @match
     (let [view (:view (:data @match))]
       [view @match]))])

(defn home-page []
  [:span
   "This is the home page"])

(defn about-page []
  [:span
   "this is the about page"])

(def routes
  [["/" {:name ::home
         :view home-page}]
   ["/about" {:name ::about
              :view about-page}]])

(defn ^:export init []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [m] (reset! match m))
   {:use-fragment true})
  (r/render [current-page]
            (js/document.getElementById "root")))

(init)
