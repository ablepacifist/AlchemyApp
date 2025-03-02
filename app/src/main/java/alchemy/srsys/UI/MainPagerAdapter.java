package alchemy.srsys.UI;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import alchemy.srsys.logic.GameManager;
import alchemy.srsys.object.Player;

public class MainPagerAdapter extends FragmentStateAdapter {

    private Player player;
    private GameManager gameManager;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity, Player player, GameManager gameManager) {
        super(fragmentActivity);
        this.player = player;
        this.gameManager = gameManager;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InventoryFragment(player, gameManager);
            case 1:
                return new KnowledgeBookFragment(player, gameManager);
            case 2:
                return new IngredientsFragment(player, gameManager);
            default:
                return new InventoryFragment(player, gameManager);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // Number of tabs
    }
}
