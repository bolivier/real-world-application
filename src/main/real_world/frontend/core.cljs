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

(defn home-banner []
  [:div.banner
   [:div.container
    [:h1.logo-font
     "conduit"
     [:p
      "A place to share your knowledge"]]]])

(def article-samples
  [{:author-name "Eric Simons"
    :profile-pic-url "http://i.imgur.com/Qr71crq.jpg"
    :publication-date "January 20th"
    :likes 29
    :title "How to build webapps that scale"
    :subtitle "This is the description for the post"}
   {:author-name "Albert Pai"
    :profile-pic-url "http://i.imgur.com/N4VcUeJ.jpg"
    :publication-date "January 20th"
    :likes 32
    :title "The song you won't ever stop singing.  No matter how hard you try."
    :subtitle "This is the description for the post"}])

(defn article-preview-area [article]
  (let [{:keys [author-name
                profile-pic-url
                publication-date
                likes
                title
                subtitle]}
        article]
    [:div.article-preview
     ^{:key (str title author-name)}
     [:div.article-meta
      [:a
       {:href "profile.html"}
       [:img {:src profile-pic-url}]]
      [:div.info
       [:a.author {:href ""}
        author-name
        [:span.date publication-date]]]
      [:button.btn.btn-outline-primary.btn-sm.pull-xs-right
       [:i.ion-heart] (str " " likes)]]
     [:a.preview-link
      [:h1 title]
      [:p subtitle]
      [:span "Read more..."]]]))

(defn home-page []
  [:div.home-page
   [home-banner]
   [:div.container.page
    [:div.row

     [:div.col-md-9
      [:div.feed-toggle
       [:ul.nav.nav-pills.outline-active
        [:li.nav-item
         [:a.nav-link.disabled
          {:href ""}
          "Your Feed"]]
        [:li.nav-item
         [:a.nav-link.active
          {:href ""}
          "Global Feed"]]]]]]
    (map #(article-preview-area %) article-samples)]])

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
