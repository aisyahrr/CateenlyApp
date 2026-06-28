package com.example.canteenlyapp.data.repository

import com.example.canteenlyapp.data.model.Canteen
import com.example.canteenlyapp.data.model.Menu
import com.example.canteenlyapp.data.model.MenuOptionGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import kotlinx.coroutines.tasks.await

class FoodRepository {
    private val food = FirebaseAuth.getInstance()
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

                        item.getValue(Canteen::class.java)
                            ?.let { canteen ->

                                val fixedCanteen = canteen.copy(
                                    isAvailable =
                                        item.child("isAvailable")
                                            .getValue(Boolean::class.java)
                                            ?: false
                                )

                                list.add(fixedCanteen)
                            }
                    }

                    onResult(list)
                }

                override fun onCancelled(error: DatabaseError) {}
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
    suspend fun updateAddress(
        canteenId: String,
        address: String
    ): Result<Unit> {

        return try {

            database
                .child(canteenId)
                .child("address")
                .setValue(address)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
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
    fun getMenusByCanteenId(
        canteenId: String,
        onResult: (List<Menu>) -> Unit
    ) {

        menuDatabase
            .orderByChild("canteenId")
            .equalTo(canteenId)
            .addValueEventListener(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        val menus =
                            mutableListOf<Menu>()

                        snapshot.children.forEach { item ->

                            item.getValue(Menu::class.java)
                                ?.let { menu ->
                                    menus.add(menu)
                                }
                        }

                        onResult(
                            menus.sortedByDescending {
                                it.createdAt
                            }
                        )
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                        onResult(emptyList())
                    }
                }
            )
    }
    fun getTotalMenusByCanteen(
        canteenId: String,
        onResult: (Int) -> Unit
    ) {

        menuDatabase
            .orderByChild("canteenId")
            .equalTo(canteenId)
            .addListenerForSingleValueEvent(

                object : ValueEventListener {

                    override fun onDataChange(
                        snapshot: DataSnapshot
                    ) {

                        onResult(
                            snapshot.childrenCount.toInt()
                        )
                    }

                    override fun onCancelled(
                        error: DatabaseError
                    ) {

                        onResult(0)
                    }
                }
            )
    }
    fun setCanteenAvailability(
        canteenId: String,
        isAvailable: Boolean,
        onResult: (Boolean) -> Unit = {}
    ) {
        database.child(canteenId).child("isAvailable")
            .setValue(isAvailable)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}

