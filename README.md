# Agri-Assist
Submission for IBM Call For Code 2021 (WiT)

### Demo Video 
https://www.youtube.com/watch?v=4IVURsN9f7Y

## Problem
Agri-Assist aims to target sustainable development goal of Zero Hunger.
Our problem statement here is that, in Food supply chain, 30% of food wastage occurs during production of crops. 
If we utilize technology to reduce this food wastage, it will help reduce the hunger problem of world. Currently, 9% of the global population is suffering from hunger. The global volume of food wastage is estimated to be 1.6 Gtonnes. This amount can be weighed against total agricultural production (for food and non-food uses), 
which is about 6 Gtonnes. 

## Idea & Solution
To solve this problem, we propose AGRI-ASSIST for minimizing farming production loss 

Agri-assist will help you choose the right crops depending on your geographical location, soil profile and weather.
It will also help forecast the diseases for the crop and suggest preventive measures for it.

![architecture](https://i.ibb.co/zswRnPF/agri-arch.png)

### Built With 

To implement our MVP, technology used is:
- **UI** - Angular
- **Backend** - Springboot hosted on Heroku
- **IBM AutoAI** - Crop recommendation & Disease prediction
- Amazon search API to recommend the most suited products.
- OpenWeather API helps in weather prediction, based upon the GPS co-ordinates which are used for prediction.

![Demo](https://github.com/leenabhandari/Wit/blob/main/Agri-assist%20demo.gif)

For crop recommendation we have used public datasets from UCI Archive & Kaggle. Data set for disease forecasting was created using research papers of IJAI & MDPI journals. Two separate models are trained on Auto AI and output of the crop prediction is used for disease forecasting.

Disease prediction alone cannot helps farmers reduce the food wastage, we need to provide them with a solution, i.e. what products can help them prevent their crops from these diseases and help in increasing production. Agri-assist picks the keywords like crop name and disease name and uses amazon search service to retrieve products available in the market. Farmers can click on the link and navigate to Amazon site, to buy these products as well.


![](https://i.ibb.co/QJxfKtV/agre-future.png)

*Solution Roadmap for Agri-Assist includes of:*
1. Localization - adding the local language support to increase the reach of this solution
2. IOT - IOT based sensors to capture the soil profile and provide accurate inputs to the application
3. DATA - Acquiring diverse and refined data for disease forecasting
4. Offline Stores - Extending product recommendations to include local stores
5. Pre & Post Harvest Care -Including suggestions for pre and post harvest care to improve land fertility

*Note that the API secret keys file is NOT uploaded on Github.*
