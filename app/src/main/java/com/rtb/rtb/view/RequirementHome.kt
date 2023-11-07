package com.rtb.rtb.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.rtb.rtb.R
import com.rtb.rtb.adapters.RequirementResumeCardAdapter
import com.rtb.rtb.databinding.ActivityRequirementHomeBinding
import com.rtb.rtb.model.Requirement
import com.rtb.rtb.view.components.AppBarFragment
import com.rtb.rtb.view.components.ButtonFragment
import com.rtb.rtb.view.components.InputFragment
import java.util.Date
import java.util.UUID

class RequirementHome : AppCompatActivity() {
    private val binding by lazy {
        ActivityRequirementHomeBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAppBar()
        setupListView()
        setupSearchInput()
        setupCreateButton()
    }

    private fun setupAppBar() {
        val appBar = supportFragmentManager.findFragmentById(R.id.app_bar) as AppBarFragment
        appBar.setupAppBar(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appBar.setModule("RMS")
        }
    }

    private fun setupListView() {
        val requirements = getRequirements()
        val requirementListView = binding.rhListViewOfRequirements
        val requirementsCardAdapter = RequirementResumeCardAdapter(this, requirements)
        requirementListView.adapter = requirementsCardAdapter
    }

    private fun setupSearchInput() {
        val searchRequirementByTitleInput = supportFragmentManager.findFragmentById(R.id.rh_text_input_search_requirement) as InputFragment
        searchRequirementByTitleInput.setHint(getString(R.string.search_requirement))

        searchRequirementByTitleInput.addTextChangedListener { title ->
            val requirements = getRequirements()
            val foundRequirements = getRequirementsByTitle(requirements, title)

            val foundRequirementsCardAdapter = RequirementResumeCardAdapter(this, foundRequirements)
            binding.rhListViewOfRequirements.adapter = foundRequirementsCardAdapter
        }
    }

    private fun setupCreateButton() {
        val createRequirement = supportFragmentManager.findFragmentById(R.id.rh_button_new_requirement) as ButtonFragment
        val createButton = createRequirement.setupButton(getString(R.string.new_requirement), Color.argb(255, 93, 63, 211))
        createButton.setOnClickListener {
            val createRequirementIntent = Intent(this, CreateRequirement::class.java)
            startActivity(createRequirementIntent)
        }
    }

    private fun getRequirementsByTitle(requirements: MutableList<Requirement>, title: String): MutableList<Requirement> {
        val foundRequirements = mutableListOf<Requirement>()
        for (requirement in requirements) {
            if (requirement.title.contains(title, ignoreCase = true)) {
                foundRequirements.add(requirement)
            }
        }
        return foundRequirements
    }

    private fun getRequirements(): MutableList<Requirement> {
        return mutableListOf(
            Requirement(
                UUID.randomUUID(),
                "RF-1",
                "The system must allow the user to log in to the system.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam nec purus ut odio cursus tristique. Vivamus et vulputate libero. Proin auctor neque eu lacinia feugiat. Cras euismod ex eget ante convallis, sed scelerisque quam aliquam. Vestibulum tristique justo non erat mattis, vel pharetra ante auctor. Aenean id purus ac urna blandit varius. Suspendisse sit amet sagittis nunc, a vehicula purus. Sed et eleifend metus. Proin convallis elit eget felis scelerisque congue. Fusce tristique, velit et vehicula tristique, dui massa hendrerit lectus, non facilisis eros tortor sit amet nulla. Vestibulum varius enim ut elit sodales, ac facilisis purus condimentum. Nullam semper tortor vel eros tincidunt, non volutpat ipsum tristique. Suspendisse potenti. Nunc feugiat libero a urna elementum, vel facilisis mi tristique. Sed accumsan arcu ac nisl fermentum vestibulum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eu faucibus nibh, ut porttitor sem. Donec placerat ultricies rutrum. Aliquam et ipsum neque. Sed pharetra accumsan nisl, at commodo nunc dignissim quis. Proin finibus magna felis, facilisis cursus lacus congue eget. Cras a ante ullamcorper, mollis nibh vitae, pharetra nulla. Nullam placerat, eros ac tempus egestas, urna orci posuere leo, vel consectetur ante velit nec dolor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed at nisi odio. Fusce ultrices, erat a maximus ultrices, sem elit maximus felis, id porta leo massa vitae magna. Vivamus hendrerit finibus purus, id pellentesque tellus vestibulum pellentesque. Quisque dolor quam, gravida sed nisl a, aliquet convallis metus. Nam rutrum posuere neque, sit amet rhoncus sem varius finibus. Sed vestibulum nibh quis euismod sollicitudin. Vestibulum id ipsum purus. Phasellus et rhoncus quam. Nunc porttitor at massa eget iaculis. In rutrum pharetra lectus sit amet scelerisque. Fusce euismod a urna in facilisis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla vel venenatis leo. Morbi dignissim ornare tellus non laoreet. Vestibulum tortor purus, auctor in fermentum quis, rhoncus sed ipsum. Nulla placerat diam non est interdum, sit amet laoreet eros bibendum. Suspendisse suscipit pellentesque finibus. In sapien urna, auctor vitae rutrum ut, rutrum non est. Sed vulputate dui sed felis ornare tincidunt. Aliquam eget nibh vitae enim viverra finibus. Maecenas ultricies mauris in consequat venenatis. Sed venenatis porttitor ligula, sed vestibulum purus blandit imperdiet. Aliquam accumsan odio non lorem luctus suscipit. Pellentesque tristique massa et dui sollicitudin ornare. Mauris consequat feugiat purus, vitae dictum mi fringilla eget. Nulla facilisi. Curabitur tincidunt enim in tempus facilisis. Ut vitae malesuada ligula. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean vestibulum est at nunc bibendum, quis bibendum mi fermentum. Nunc ultrices turpis sapien, non posuere ligula tempor in. Ut imperdiet laoreet felis, non auctor est dapibus id. Aliquam dictum, ex ultrices cursus pellentesque, quam turpis semper nunc, eget vehicula libero lectus vel erat. Nullam aliquam dignissim est vitae euismod. Interdum et malesuada fames ac ante ipsum primis in faucibus. Fusce rutrum metus felis, quis congue neque egestas vel. Aliquam efficitur augue eget accumsan convallis. Nam gravida porttitor tincidunt. Sed viverra augue eros, sit amet lobortis nisi pretium sed.",
                "As a registered user...\nI want to log in to the system...\nSo that I can access my account and use the system's features.",
                "Functional requirement",
                "Product",
                "High",
                UUID.randomUUID(),
                Date(),
                Date(),
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-2",
                "The system must support multi-language capabilities.",
                "This requirement ensures that the application can be used by people who speak different languages.",
                "As a user of the application, I want to use it in my preferred language to understand the content and interact with the system effectively.",
                "Functional requirement",
                "Product",
                "Medium",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-3",
                "The system should have a user-friendly interface.",
                "A user-friendly interface enhances the user experience and makes the system more intuitive.",
                "As a user, I want to interact with the system through an interface that is easy to navigate and understand.",
                "Usability requirement",
                "Product",
                "High",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-5",
                "The system must support user profile customization.",
                "Users should be able to personalize their profiles with avatars, names, and other details.",
                "As a registered user, I want to customize my profile to make it more personalized and reflective of my preferences.",
                "User customization requirement",
                "Product",
                "Medium",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-6",
                "The system should have a real-time chat feature.",
                "Real-time chat allows users to communicate with each other instantly.",
                "As a user, I want to chat with other users in real-time within the system to facilitate quick communication and collaboration.",
                "Feature requirement",
                "Product",
                "High",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-7",
                "The system must have a notification system.",
                "Notifications keep users informed about important events and updates.",
                "As a user, I want to receive notifications about system updates, messages, and other relevant information to stay informed.",
                "Notification requirement",
                "Product",
                "Medium",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-8",
                "The system should support offline mode.",
                "Offline mode allows users to access certain features even when not connected to the internet.",
                "As a user, I want to be able to use the system's core features in offline mode to ensure uninterrupted access.",
                "Offline capability requirement",
                "Product",
                "High",
                UUID.randomUUID(),
                Date(),
                Date()
            ),
            Requirement(
                UUID.randomUUID(),
                "RF-9",
                "The system should have a dark mode option.",
                "Dark mode enhances usability in low-light environments and reduces eye strain.",
                "As a user, I want the option to switch to dark mode for a more comfortable viewing experience in low-light conditions.",
                "User preference requirement",
                "Product",
                "Low",
                UUID.randomUUID(),
                Date(),
                Date()
            )
        )
    }
}
