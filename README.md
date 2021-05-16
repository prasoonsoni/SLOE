# SLOE: Supervised Learning in Online Education
> ## Cognized Online Education || A Blunder Bois Project

### <img src="https://freeiconshop.com/wp-content/uploads/edd/code-flat.png" width="32" height="32"> APKs
The project APK can be downloaded from [here](https://drive.google.com/drive/folders/1rxymNwMUUjd5hsnT7Pt_AyPrFKuqkH7_?usp=sharing).

### <img src="https://cdn.iconscout.com/icon/free/png-256/idea-1618744-1372450.png" width="32" height="32"> Abstract
With the current pandemic going on, education has shifted to online mode, which has a lot of downsides. Amidst all of this, we have a positive service as an add-on to the current medium of online classes. Childhood depression is something that is underlooked and has long-term effects, including the mental growth of a person. There are many reasons which lead to a child’s abnormal behavior which cannot be known unless the child tells, like bullying, or getting scolded by a teacher, and even arguments between parents, and especially during these tough times it is really hard to cope up with mental health. Sometimes abnormalities get noticed, but most of the time, child won’t show such traits in front of parents and absorb what he/she is feeling.

### <img src="https://image.flaticon.com/icons/png/512/1632/1632633.png" width="32" height="32"> Objective
Mental health is a very serious issue, which needs to be looked upon with utmost care. To create virtual access to a child’s mental health we have come up with our service - SLOE. As education is online, students sit hours in front of screens, and that’s the only time most of them are interacting with the outer world. During this time, without them noticing we record their behavioral pattern, and notify parents if there is an anomaly in the behavioral pattern of their child, or in simpler terms a sudden change in the behavior of their child during classes.

### <img src="https://www.pinclipart.com/picdir/big/352-3523258_implementation-icon-implementation-mechanism-vector-clipart.png" width="40" height="32"> Implementation
To record the child’s behavior, we will be using the video feed shared by the child’s device and will capture snapshots from it. Those snapshots will be fed into our Deep Learning model, which will label them with tags: Attentive, Unattentive, or Confused. Those tags are what we will be working on, and hence they will be saved in a database. Using the tags makes further makes our app very efficient. Further, we will be using Anomaly Detection on child’s data. As we need to train a model, for the first 10 trial days we’ll only collect the data, and all of this will be stored on which we’ll train our Anomaly Detection model using Stochastic Gradient Descent. From the 11th day, after every class, we’ll run Anomaly Detection on that class’s data, and its analysis will be displayed on our app. If our app detects any Anomaly, it’ll notify the user (parents) about the anomaly. Parents can see the analysis on the dashboard. Our dashboard will show parents complete analysis over time in a calendar view, highlighted in a color scheme to ease the process of finding a day with the anomaly, and the days can be further clicked on to receive a detailed analysis of every lecture in that day classified with a tag: Attentive, Unattentive, Confused, Depressed or, Happy/cheerful, based on child’s behavior. Apart from these statistics, we’ll also be providing some extra features by performing EDA on the recorded data, but they’ll be a part of our PRO membership and will be explained in detail in the Business Model.
using EDA on the recorded data, we’ll also be able to show a k-means graph, which will help parents.

### <img src="https://noveltypharma.eu/wp-content/uploads/2020/10/icon_novel_ingredients.png" width="32" height="32"> Novelty
There are many projects, which can do Attentivity checking. But we have an advanced project which apart from doing Attention checking, does mood recognition and focuses on the mental health of children during these tough times. The mental development of a child occurs most in these foundational stages, and such effects on mental health at early stages can have a lifelong impact, and can even have a negative impact on a child’s attitude in the future. Utmost importance has to be given to a child’s mental health at these stages, and our app helps in doing so by breaking the barrier.

### <img src="https://icons-for-free.com/iconfiles/png/512/workflow-131964753379858822.png" width="40" height="32"> Workflow
1. Organization/Parent registers the child.
2. The app captures the image/snapshots from a live feed.
3. Using deep learning algorithms, the app detects the mood of the child and will give a label accordingly.
4. The label is stored in a database along with the time-stamp.
5. A machine learning model, trained on a dataset of that child will look up for anomalies in child behavior.
6. All of the data will be shown in the dashboard, along with tags and color-coded. Also, the user will be notified of an anomaly.
7. The user can also look up at his/her previous data.

### <img src="https://cdn1.iconfinder.com/data/icons/currency-and-symbols-1/70/coin-10-512.png" width="32" height="32"> Business Model
Our app will give user choice to choose from two plans :
#### (i) A free tier - Basic plan and 
#### (ii) A paid - Pro Plan. 

Our service is aimed for betterment of  every child, so we wont be charging any amount for Anomaly detection. Free tier plan includes, a limited storage database, refreshed every 1 month. And notification for anomaly detection. Our Pro plan currently will include, customized EDA graphs(k-means) for the child, which can help parents and child to see during what time child has maximum concentration and other analysis. Apart from that, we’ll also provide live anomaly detection using cloud services. For Basic plan, anomaly detection will be performed after the class. Also, we’ve few beta features in pro plan, including detailed minute per minuter analytics on dashboard for every lecture.

### <img src="https://www.starface.com/wp-content/uploads/2017/05/STARFACE_Comfortphoning_Zukunftssicher-Icon-350-1.png" width="40" height="32"> Future scope
The app is currently aimed only for students, but we aim to use data of all the students of a class and perform k-means graph, on teachers side, so that teacher can also come to know at which topic students didn’t understand, as well as same can be used by higher authorities to check teacher’s ability to make students understand. This is another serious issue, as some teachers just read from textbook and hence many students don’t understand concepts which forms the base for newer topics, and also it is the primary reason of many students lacking interest in studying.

### <img src="https://techstackapps.com/media/2019/11/TechStackApps-logo-icon.png" width="32" height="32"> Tech stack
1. <img src="https://2.bp.blogspot.com/-tzm1twY_ENM/XlCRuI0ZkRI/AAAAAAAAOso/BmNOUANXWxwc5vwslNw3WpjrDlgs9PuwQCLcBGAsYHQ/s1600/pasted%2Bimage%2B0.png" width="32" height="32">  Android Studio
2. <img src="https://developers.google.com/ml-kit/images/homepage/hero.png" width="40" height="32"> Google ML Kit
3. <img src=https://www.gstatic.com/devrel-devsite/prod/v1674d466be3b1154327dd11cf186e748303b1e92ae31ff35df0f5192fbd777ea/firebase/images/touchicon-180.png width="32" height="32"> Firebase
4. <img src=https://codelabs.developers.google.com/codelabs/recognize-flowers-with-tensorflow-on-android/img/657431be3173fa86.png width="32" height="32"> TensorFlow Lite
5. <img src=https://docs.streamlit.io/en/stable/_static/logomark_website.png width="32" height="32"> Streamlit
6. <img src=https://cdn.shopify.com/s/files/1/0284/7024/7555/products/figma2x_1048x.png width="32" height="32"> Figma
7. <img src=https://cdn.iconscout.com/icon/free/png-512/heroku-5-569467.png width="32" height="32"> Heroku
