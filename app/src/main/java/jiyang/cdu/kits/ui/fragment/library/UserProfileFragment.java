package jiyang.cdu.kits.ui.fragment.library;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jiyang.cdu.kits.R;
import jiyang.cdu.kits.databinding.FragmentLibraryProfileBinding;
import jiyang.cdu.kits.model.enty.Book;
import jiyang.cdu.kits.model.enty.LibraryUserInfo;
import jiyang.cdu.kits.presenter.library.LibraryBookListImpl;
import jiyang.cdu.kits.presenter.library.LibraryBookListPresenter;
import jiyang.cdu.kits.ui.common.AdapterItem;
import jiyang.cdu.kits.ui.common.BaseFragment;
import jiyang.cdu.kits.ui.common.CommAdapter;
import jiyang.cdu.kits.ui.view.library.LibraryBookListView;
import jiyang.cdu.kits.ui.widget.UiUtils;

public class UserProfileFragment extends BaseFragment implements LibraryBookListView, SwipeRefreshLayout.OnRefreshListener {
    @Override
    public String getTitle() {
        return "个人";
    }

    public static UserProfileFragment instance(LibraryUserInfo userInfo) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", userInfo);
        userProfileFragment.setArguments(bundle);
        return userProfileFragment;
    }

    private FragmentLibraryProfileBinding binding;
    private List<Book> books;
    private LibraryBookListPresenter libraryBookListPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_library_profile, container, false);
        LibraryUserInfo userInfo = (LibraryUserInfo) getArguments().getSerializable("userInfo");
        if (userInfo != null) {
            init(userInfo);
        }
        return binding.getRoot();
    }

    private void init(LibraryUserInfo userInfo) {
        binding.userName.setText(userInfo.name);
        binding.userClass.setText(userInfo.learnType);
        binding.sNo.setText(userInfo.account);
        binding.bookedCount.setText(userInfo.bookCount);
        binding.illegalCount.setText(userInfo.wrongCount);
        binding.maxBookCount.setText(userInfo.maxBooks);
        binding.maxOrderCount.setText(userInfo.maxPlanBooks);
        books = new ArrayList<>();
        CommAdapter<Book> bookCommAdapter = new CommAdapter<Book>(books) {

            @Override
            public AdapterItem<Book> createItem() {
                return new AdapterItem<Book>() {
                    TextView bookName, getDate, outDate, borrowInfo, renewCount;
                    Button renew;

                    @Override
                    public void handleData(final int position) {
                        final Book data = books.get(position);
                        bookName.setText(data.name);
                        getDate.setText(String.format(UserProfileFragment.this.getString(R.string.book_date), data.getData));
                        outDate.setText(String.format(UserProfileFragment.this.getString(R.string.book_end_date), data.endData));
                        borrowInfo.setText(String.format(UserProfileFragment.this.getString(R.string.book_out_of_date), data.getOutOfDateDays()));
                        renewCount.setText(String.format(UserProfileFragment.this.getString(R.string.book_times), data.getCount));
                        renew.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UserProfileFragment.this.libraryBookListPresenter.reBook(data, position);
                            }
                        });
                    }

                    @Override
                    public int getResId() {
                        return R.layout.item_booklist;
                    }

                    @Override
                    public void bindViews(View itemView) {
                        bookName = itemView.findViewById(R.id.tv_book_name);
                        getDate = itemView.findViewById(R.id.tv_book_getDate);
                        outDate = itemView.findViewById(R.id.tv_book_outDate);
                        borrowInfo = itemView.findViewById(R.id.tv_borrow_info);
                        renewCount = itemView.findViewById(R.id.tv_renew_count);
                        renew = itemView.findViewById(R.id.renew);
                    }
                };
            }
        };
        binding.refreshLayout.setOnRefreshListener(this);
        setRefreshLayoutColor(binding.refreshLayout);
        binding.recycleView.setAdapter(bookCommAdapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        libraryBookListPresenter = new LibraryBookListImpl(this);
        libraryBookListPresenter.fetchBookList();
        isFirstCome("下拉可刷新，点击续借快速续借");
    }


    @Override
    public void onSuccess(List<Book> bookList) {
        if (bookList == null || bookList.size() == 0) {
            return;
        }
        this.books.clear();
        this.books.addAll(bookList);
        binding.recycleView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        binding.refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        binding.refreshLayout.setRefreshing(false);
    }

    @Override
    public void showError(String error) {
        UiUtils.showErrorSnackbar(getContext(), binding.getRoot(), error);
    }

    @Override
    public void onReBookSuccess(int position) {
        this.books.get(position).getCount += 1;
        binding.recycleView.getAdapter().notifyItemChanged(position);
    }

    @Override
    public void onRefresh() {
        libraryBookListPresenter.fetchBookList();
    }
}
