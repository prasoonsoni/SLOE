# -*- coding: utf-8 -*-
"""
Created on Fri May 14 23:00:38 2021

@author: romas
"""

import streamlit as st
import pickle
import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegressionCV
Data=pd.read_csv("studentbehaviour_Anomaly/edu1.csv")



pickle_in = open("lm.pkl","rb")
classifier=pickle.load(pickle_in)
def mood_recog(Day,Month,Year,Lecture_1,Lecture_2,Lecture_3,Lecture_4,Lecture_5):
    prediction=classifier.predict([[Day,Month,Year,Lecture_1,Lecture_2,Lecture_3,Lecture_4,Lecture_5]])
    print(prediction)
    return prediction
def main():
    st.title("SLOE ML Web App")
    html_temp = """
    <div style="background-color:tomato;padding:10px">
    <h2 style="color:blue;text-align:center;">Student Mood Recognition </h2>
    </div>
    """
    name = st.text_input("Enter your Child's name: ")
    email = st.text_input("Enter your Email-id: ")
    day = st.text_input("Enter Day: ")
    month=st.text_input("Enter Month: ")
    year=st.text_input("Enter Year: ")
    if st.button("Know More"):
        html_temp = """
                        
                        <div>
                        <h3 style="color:red;text-align:left;">0 =  Attentive </h3>
                        <h3 style="color:red;text-align:left;">1 = Unattentive </h3>
                        <h3 style="color:red;text-align:left;">2 = Confused </h3>
                        <h3 style="color:red;text-align:left;">3 = Depressed</h3>
                        <h3 style="color:red;text-align:left;">4 = Cheerful </h3> 

                       </div>
                    """
        st.markdown(html_temp,unsafe_allow_html=True)
    if st.button("Predict"):
        result = mood_recog(day,month,year,0,0,1,0,4)
        if int(result)==0:
            st.success("Your child was Attentive during the day.")
        if int(result)==1:
            st.success("Your child was Unattentive during the day.")
        if int(result)==2:
            st.success("Your child was Confused during the day.")
        if int(result)==3:
            st.success("Your child was Depressed during the day.")
        if int(result)==4:
            st.success("Your child was Happy during the day.")
        import ics as icsneo
        import calendar
        from ics import Calendar, Event
        final = str(year) + "-" +str(month) + "-" +str(day) + " 00:00:00"
        print(final)
        c = Calendar()
        e = Event()
        e.name = "Mood of your child today:"
        e.begin = final
        c.events.add(e)
        c.events
        with open('my.ics', 'w') as my_file:
            my_file.writelines(c)
        import smtplib
        from email.message import EmailMessage
    
        EMAIL_ADDRESS ="weecaree4youu@gmail.com"
        EMAIL_PASSWORD ="glvqfnhrbcjdjsqc"
    
        msg = EmailMessage()
        msg['Subject'] = 'Your child daily behaviour: '
        msg['From'] = EMAIL_ADDRESS
        msg['To'] = email
    
        msg.set_content('Your child was Happy during the day.')
    
        files = ['my.ics']
    
        for file in files:
            with open(file,'rb') as f:
                file_data = f.read()
                file_name = f.name
    
            msg.add_attachment(file_data, maintype='application', subtype='octet-stream',filename=file_name)
    
        with smtplib.SMTP_SSL('smtp.gmail.com', 465) as smtp:
            smtp.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
            smtp.send_message(msg)
        st.line_chart(data=Data, width=0, height=0, use_container_width=True)
        st.bar_chart(Data)
        



if __name__=='__main__':
    main()
    
        
    

