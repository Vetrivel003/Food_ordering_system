import streamlit as st
import requests

BASE_URL = "http://localhost:8081/api"

if "access_token" not in st.session_state:
    st.session_state.access_token = None

if "refresh_token" not in st.session_state:
    st.session_state.refresh_token = None

if "user_email" not in st.session_state:
    st.session_state.user_email = None

if "selected_restaurant" not in st.session_state:
    st.session_state.selected_restaurant = None

def get_headers():
    if st.session_state.access_token:
        return {
            "Authorization": f"Bearer {st.session_state.access_token}"
        }
    return {}


def login_user(email, password):
    response = requests.post(
        f"{BASE_URL}/auth/login",
        json={"email": email, "password": password}
    )

    if response.status_code == 200:
        data = response.json()
        st.session_state.access_token = data["accessToken"]
        st.session_state.refresh_token = data["refreshToken"]
        st.session_state.user_email = data["email"]
        st.success("Login successful")
    else:
        st.error(response.json().get("message", "Login failed"))


def register_user(name, email, password):
    response = requests.post(
        f"{BASE_URL}/auth/register",
        json={
            "name": name,
            "email": email,
            "password": password
        }
    )

    if response.status_code == 200:
        st.success("Registration successful. You can now login.")
    else:
        st.error(response.json().get("message", "Registration failed"))


def logout_user():
    if st.session_state.refresh_token:
        requests.post(
            f"{BASE_URL}/auth/logout",
            json={"refreshToken": st.session_state.refresh_token}
        )

    st.session_state.access_token = None
    st.session_state.refresh_token = None
    st.session_state.user_email = None
    st.success("Logged out successfully")


st.title("🍽️ Sapaadu Food Ordering System")

if st.session_state.access_token:

    st.sidebar.success(f"Logged in as: {st.session_state.user_email}")

    menu = st.sidebar.selectbox(
        "Navigation",
        ["Restaurants", "Logout"]
    )

    if menu == "Logout":
        logout_user()

    elif menu == "Restaurants":

        st.subheader("Available Restaurants")

        response = requests.get(
            f"{BASE_URL}/restaurants",
            headers=get_headers()
        )

        if response.status_code == 200:
            restaurants = response.json()

            if not restaurants:
                st.info("No restaurants available.")
            else:
                for r in restaurants:
                    with st.container():
                        st.markdown(f"### 🍴 {r['name']}")
                        st.write(f"📍 Description: {r['description']}")

                        if st.button(
                            f"View Menu - {r['id']}",
                            key=f"view_{r['id']}"
                        ):
                            st.session_state.selected_restaurant = r

                        st.divider()

        elif response.status_code == 401:
            st.error("Session expired. Please login again.")
        else:
            st.error("Failed to fetch restaurants.")

        if st.session_state.selected_restaurant:

            restaurant = st.session_state.selected_restaurant
            st.subheader(f"Menu - {restaurant['name']}")

            menu_response = requests.get(
                f"{BASE_URL}/restaurants/{restaurant['id']}/menu",
                headers=get_headers()
            )

            if menu_response.status_code == 200:
                menu_items = menu_response.json()

                if not menu_items:
                    st.info("No menu items available.")
                else:
                    for item in menu_items:
                        with st.container():
                            st.markdown(f"**{item['name']}**")
                            st.write(f"Price: ₹ {item['price']}")
                            st.divider()
            else:
                st.error("Failed to load menu items.")

else:
    page = st.sidebar.radio("Select Option", ["Login", "Register"])

    if page == "Login":
        st.subheader("Login")

        email = st.text_input("Email")
        password = st.text_input("Password", type="password")

        if st.button("Login"):
            login_user(email, password)

    elif page == "Register":
        st.subheader("Register")

        name = st.text_input("Name")
        email = st.text_input("Email")
        password = st.text_input("Password", type="password")

        if st.button("Register"):
            register_user(name, email, password)