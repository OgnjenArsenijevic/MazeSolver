#include <iostream>
#include <utility>
#include <vector>
#include <queue>
#include <functional>
#include <cstring>
#include <cstdio>
#include <algorithm>
#define INF 1061109567

using namespace std;

vector<pair<int,int>> g[1450];
int dis[1450],par[1450],i1,j1,i2,j2,w;
priority_queue<pair<int,int>, vector<pair<int,int>> ,greater<pair<int,int>>> pq;
vector<pair<pair<int,int>,int> > ans;

int main()
{
    freopen("file1.txt","r",stdin);
    freopen("file2.txt","w",stdout);
    while(cin>>i1)
    {
        cin>>j1>>i2>>j2>>w;
        g[i1*100+j1].push_back(make_pair(w,i2*100+j2));
        g[i2*100+j2].push_back(make_pair(w,i1*100+j1));
    }
    memset(dis,63,sizeof(dis));
    dis[0]=0;
    par[0]=-1;
    pq.push(make_pair(0,0));
    while(!pq.empty())
    {
        int d=pq.top().first;
        int u=pq.top().second;
        pq.pop();
        if(d>dis[u])
            continue;
        for(int i=0;i<g[u].size();i++)
        {
            int w=g[u][i].first;
            int v=g[u][i].second;
            if(dis[u]+w<dis[v])
            {
                dis[v]=dis[u]+w;
                par[v]=u;
                pq.push(make_pair(dis[v],v));
            }
        }
    }
    if(dis[1414]==INF)
        cout<<"-1 -1 -1\n";
    else
    {
        ans.push_back(make_pair(make_pair(14,14),dis[1414]));
        int curr=1414;
        while(par[curr]!=-1)
        {
            curr=par[curr];
            ans.push_back(make_pair(make_pair(curr/100,curr%100),dis[curr]));
        }
        reverse(ans.begin(),ans.end());
    }
    for(int i=0;i<ans.size();i++)
        cout<<ans[i].first.first<<" "<<ans[i].first.second<<" "<<ans[i].second<<"\n";
    return 0;
}
