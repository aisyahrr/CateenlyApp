package com.example.canteenlyapp.data.repository

import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.data.model.MenuOptionGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class FoodRepository {

    private val database =
        FirebaseDatabase
            .getInstance()
            .getReference("canteens")

    private val menuDatabase =
        FirebaseDatabase
            .getInstance()
            .getReference("menus")

    private val menuOptionDatabase =
        FirebaseDatabase
            .getInstance()
            .getReference("menu_options")
    fun getCanteens(
        onResult: (List<Canteen>) -> Unit
    ) {

        database.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = mutableListOf<Canteen>()

                    for (item in snapshot.children) {

                        val canteen =
                            item.getValue(Canteen::class.java)

                        if (canteen != null) {
                            list.add(canteen)
                        }
                    }

                    onResult(list)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }

    fun getCanteenById(
        canteenId: String,
        onResult: (Canteen?) -> Unit
    ) {

        database
            .child(canteenId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        val canteen =
                            snapshot.getValue(
                                Canteen::class.java
                            )

                        onResult(canteen)
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {
                        onResult(null)
                    }
                }
            )
    }
    fun getMenus(
        onResult: (List<Menu>) -> Unit
    ) {

        menuDatabase.addValueEventListener(
            object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = mutableListOf<Menu>()

                    for (item in snapshot.children) {

                        val menu = item.getValue(Menu::class.java)

                        if (menu != null) {

                            val fixedMenu = menu.copy(
                                isBestSeller =
                                    item.child("isBestSeller")
                                        .getValue(Boolean::class.java)
                                        ?: false
                            )

                            list.add(fixedMenu)
                        }
                    }

                    onResult(list)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
        )
    }
    fun getMenuOptions(
        menuId: String,
        onResult: (List<MenuOptionGroup>) -> Unit
    ) {

        menuOptionDatabase
            .child(menuId)
            .addListenerForSingleValueEvent(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        val groups =
                            mutableListOf<MenuOptionGroup>()

                        for (item in snapshot.children) {

                            val group =
                                item.getValue(
                                    MenuOptionGroup::class.java
                                )

                            if (group != null) {
                                groups.add(group)
                            }
                        }

                        onResult(groups)
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {
                        onResult(emptyList())
                    }
                }
            )
    }
}

